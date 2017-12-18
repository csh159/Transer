package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public abstract class BaseTaskHandler implements ITaskHandler {

    protected ITaskHandlerListenner mListenner;
    private boolean isExit = false;
    private Map<String, String> mParams;
    private Map<String, String> mHeaders;
    private ExecutorService mTaskHandleThreadPool;
    private Task mTask;
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
        mTask.setState(state);
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


    protected  abstract boolean isPiceSuccessful();

    protected abstract boolean isSuccessful();

    protected abstract byte[] readPice(Task task) throws Exception;

    protected abstract void writePice(byte[] datas,Task task) throws Exception;

    protected abstract void prepare(Task task) throws Exception;

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
        isExit = false;
        mTaskHandleThreadPool.execute(mHandleRunnable);
        mListenner.onStart(mTask);
    }

    @Override
    public void stop() {
        isExit = true;
        mListenner.onStop(mTask);
    }

    @Override
    public void pause() {
        isExit = true;
        mListenner.onPause(mTask);
    }

    @Override
    public void resume() {
        isExit = false;
        mTaskHandleThreadPool.execute(mHandleRunnable);
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
