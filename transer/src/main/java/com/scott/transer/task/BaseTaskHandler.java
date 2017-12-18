package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:
 *      如果实现其他的传输器需要继承自该类
 * </p>
 */
public abstract class BaseTaskHandler implements ITaskHandler {

    protected ITaskHandlerListenner mListenner;
    private volatile boolean isExit = false;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;
    private ExecutorService mTaskHandleThreadPool;
    private volatile Task mTask;
    private HandleRunnable mHandleRunnable;

    public BaseTaskHandler() {
        mHandleRunnable = new HandleRunnable();
    }

    @Override
    public void setHandlerListenner(ITaskHandlerListenner l) {
        mListenner = l;
    }

    @Override
    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    @Override
    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    @Override
    public void setThreadPool(ExecutorService threadPool) {
        mTaskHandleThreadPool = threadPool;
    }

    @Override
    public void setState(int state) { //user call
        _internalSetState(state);
    }

    private void _internalSetState(int state) {
        switch (state) {
            case TaskState.STATE_START:
                start();
                break;
            case TaskState.STATE_STOP:
                stop();
                break;
            case TaskState.STATE_PAUSE:
                pause();
                break;
            case TaskState.STATE_RESUME:
                resume();
                break;
        }
    }

    //判断一片是否发送或接受成功
    protected  abstract boolean isPiceSuccessful();

    //判断任务是否成功
    protected abstract boolean isSuccessful();

    //从数据源中读取一片
    protected abstract byte[] readPice(Task task) throws Exception;

    //写入一片到目标
    protected abstract void writePice(byte[] datas,Task task) throws Exception;

    //传输开始前
    protected abstract void prepare(Task task) throws Exception;

    //当前这片从数据源中实际读取的大小
    protected abstract int getPiceRealSize();

    private void handle(ITask task) throws Exception {
        prepare((Task) task);

        while (!isExit) {
            byte[] datas = readPice((Task) task);
            int size = getPiceRealSize();

            if(size == -1) {
                isExit = true;
                break;
            }

            ((Task) task).setEndOffset(task.getStartOffset() + size);
            writePice(datas,(Task) task);
            mTask.setCompleteLength(mTask.getCompleteLength() + size);
            mTask.setStartOffset(mTask.getCompleteLength());

            if(isPiceSuccessful()) {
                mListenner.onPiceSuccessful(mTask);
            } else {
                mListenner.onError(TaskErrorCode.ERROR_PICE,mTask);
                isExit = true;
                break;
            }
        }

        if(!isSuccessful()) {
            mListenner.onError(TaskErrorCode.ERROR_FINISH,mTask);
        } else {
            mListenner.onFinished(mTask);
        }

        release();
    }

    @Override
    public void start() {
        synchronized (this) {
            if(mTask.getState() == TaskState.STATE_START) {
                throw new IllegalStateException("current handler already started ...");
            }
            //如果设置了线程池则会在线程池中传输，否则会在当前线程中开始传输
            if (mTaskHandleThreadPool != null) {
                mTaskHandleThreadPool.execute(mHandleRunnable);
            } else {
                mHandleRunnable.run();
            }
            mListenner.onStart(mTask);
            mTask.setState(TaskState.STATE_START);
        }
    }

    @Override
    public void stop() {

        if(TaskState.STATE_STOP == mTask.getState()) {
            return;
        }
        isExit = true;
        mTask.setState(TaskState.STATE_STOP);
        mListenner.onStop(mTask);
    }

    @Override
    public void pause() {
        if(TaskState.STATE_PAUSE == mTask.getState()) {
            return;
        }
        mTask.setState(TaskState.STATE_PAUSE);
        isExit = true;
        mListenner.onPause(mTask);
    }

    @Override
    public void resume() {

        if(mTask.getState() == TaskState.STATE_START) {
            return;
        }

        if(mHandleRunnable != null) {
            mHandleRunnable = new HandleRunnable();
        }

        start();
        mListenner.onResume(mTask);
    }

    protected void release() {
        isExit = true;
    }

    @Override
    public int getState() {
        return mTask.getState();
    }

    @Override
    public ITask getTask() {
        return mTask;
    }

    @Override
    public void setTask(ITask task) {
        mTask = (Task) task;
    }

    @Override
    public TaskType getType() {
        return mTask.getType();
    }

    class HandleRunnable implements Runnable {

        @Override
        public void run() {
            try {
                handle(mTask);
            } catch (Exception e) {
                e.printStackTrace();
                mListenner.onError(TaskErrorCode.ERROR_CODE_EXCEPTION,mTask);
            }
        }
    }
}
