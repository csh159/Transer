package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:29</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskHolderProxy implements ITaskHolderProxy {

    private ITaskHandler mHandler;

    @Override
    public void setState(int state) {
        switch (state) {
            case TaskState.STATE_STOP:
                mHandler.stop();
                break;
            case TaskState.STATE_PAUSE:
                mHandler.pause();
                break;
            case TaskState.STATE_RESUME:
                mHandler.resume();
                break;
            case TaskState.STATE_START:
                mHandler.start();
                break;
        }
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public ITask getTask() {
        return null;
    }

    @Override
    public void setTask(ITask task) {

    }

    @Override
    public TaskType getType() {
        return null;
    }

    @Override
    public void setTaskHandler(ITaskHandler handler) {

    }

    @Override
    public void setThreadPool(ExecutorService threadPool) {

    }

    @Override
    public ITaskHandler getTaskHandler() {
        return null;
    }
}
