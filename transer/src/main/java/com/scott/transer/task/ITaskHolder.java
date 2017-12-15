package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 13:15</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskHolder {
    void start();

    void stop();

    void pause();

    void resume();

    int getState();

    ITask getTask();

    void setTask(ITask task);

    TaskType getType();
}
