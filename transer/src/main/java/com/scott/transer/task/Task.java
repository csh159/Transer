package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 17:51</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class Task implements ITask {

    private ITaskBuilder mBuilder;

    public Task(ITaskBuilder builder) {
        mBuilder = builder;
    }

    @Override
    public String getDataSource() {
        return mBuilder.getDataSource();
    }

    @Override
    public String getDestSource() {
        return mBuilder.getDestSource();
    }

    @Override
    public String getSesstionId() {
        return null;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public long getStartOffset() {
        return 0;
    }

    @Override
    public long getEndOffset() {
        return 0;
    }

    @Override
    public String getTaskId() {
        return null;
    }

    @Override
    public String getGroupId() {
        return null;
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public long getCompleteTime() {
        return 0;
    }

    @Override
    public long getCompleteLength() {
        return 0;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public TaskType getType() {
        return null;
    }

    @Override
    public String getUserId() {
        return null;
    }
}
