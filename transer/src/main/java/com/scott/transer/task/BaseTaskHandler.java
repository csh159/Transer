package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public abstract class BaseTaskHandler implements ITaskInternalHandler {

    @Override
    public void setThreadPool(ExecutorService threadPool) {

    }

    @Override
    public void setState(int state) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

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

}
