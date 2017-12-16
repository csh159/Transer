package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskType;

import java.util.List;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/16</P>
 * <P>Email: shilec@126.com</p>
 */

public class TaskCmd implements ITaskCmd{

    private ITaskCmdBuilder mBuilder;

    public TaskCmd(ITaskCmdBuilder builder) {
        mBuilder = builder;
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
    public int getState() {
        return 0;
    }

    @Override
    public ITask getTask() {
        return mBuilder.getTask();
    }

    @Override
    public List<ITask> getTasks() {
        return mBuilder.getTasks();
    }

    @Override
    public String[] getTaskIds() {
        return new String[0];
    }

    @Override
    public ProcessType getOperationType() {
        return mBuilder.getOperationType();
    }

    @Override
    public TaskType getTaskType() {
        return mBuilder.getTaskType();
    }
}
