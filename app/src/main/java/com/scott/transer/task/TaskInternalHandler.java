package com.scott.transer.task;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskInternalHandler implements ITaskInternalHandler {
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

    @Override
    public void handle(String src, String dest) {

    }

    @Override
    public int handlePice(String src, String dest, long start, long end) {
        return 0;
    }

    @Override
    public boolean isPiceSuccessful(String response) {
        return false;
    }

    @Override
    public boolean isSuccessful(String response) {
        return false;
    }

    @Override
    public Runnable toRunnable() {
        return null;
    }
}
