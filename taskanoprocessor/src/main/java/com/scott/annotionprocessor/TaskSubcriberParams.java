package com.scott.annotionprocessor;

import java.util.Arrays;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-15 11:12</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskSubcriberParams {

    TaskType taskType;

    ProcessType[] processType;

    boolean hasExtas = false;

    public ITaskEventDispatcher dispatcher;

    public ThreadMode threadMode;

    @Override
    public String toString() {
        return "TaskSubcriberParams{" +
                "taskType=" + taskType +
                ", processType=" + Arrays.toString(processType) +
                ", hasExtas=" + hasExtas +
                '}';
    }
}
