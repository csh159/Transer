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

public class TaskHandlerHolder implements ITaskHandlerHolder {

    private ITaskHandler mHandler;
    private ITask mTask;

    @Override
    public void setState(int state) {
        if(mHandler != null) {
            mHandler.setState(state);
        }
    }

    @Override
    public int getState() {
        if(mHandler == null) {
            return TaskState.STATE_STOP;
        }
        return mHandler.getState();
    }

    @Override
    public ITask getTask() {
        if(mHandler == null) {
            return mTask;
        }
        return mHandler.getTask();
    }

    @Override
    public void setTask(ITask task) {
        mTask = task;
        if(mHandler != null) {
            mHandler.setTask(task);
        }
    }

    @Override
    public TaskType getType() {
        if(mHandler == null) {
            return  mTask.getType();
        }
        return mHandler.getType();
    }

    @Override
    public void setTaskHandler(ITaskHandler handler) {
        mHandler = handler;
    }

    @Override
    public ITaskHandler getTaskHandler() {
        return mHandler;
    }
}
