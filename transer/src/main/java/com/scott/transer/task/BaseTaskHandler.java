package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public abstract class BaseTaskHandler implements ITaskInternalHandler {

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

    protected abstract byte[] readPice(Task task) throws IOException;

    protected abstract void writePice(byte[] datas,Task task) throws IOException;

    protected abstract void prepare(Task task) throws IOException;

    @Override
    public void handle(ITask task) throws IOException {

        prepare((Task) task);
        while (!isExit) {
            byte[] datas = readPice((Task) task);
            writePice(datas, (Task) task);
            mTask.setCompleteLength(mTask.getEndOffset());
            mTask.setStartOffset(mTask.getEndOffset());
            if(isPiceSuccessful()) {
                mListenner.onPiceSuccessful(mTask);
            } else {
                mListenner.onError(TaskErrorCode.ERROR_PICE,mTask);
                isExit = true;
                break;
            }

            if(!isSuccessful()) {
                mListenner.onError(TaskErrorCode.ERROR_FINISH,mTask);
                isExit = true;
                break;
            } else {
                mListenner.onFinished(mTask);
                isExit = true;
                break;
            }
        }
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
            } catch (IOException e) {
                e.printStackTrace();
                mListenner.onError(TaskErrorCode.ERROR_CODE_EXCEPTION,mTask);
            }
        }
    }
}
