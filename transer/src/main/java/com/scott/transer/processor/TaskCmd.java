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

    TaskCmd(ITaskCmdBuilder builder) {
        mBuilder = builder;
    }

    @Override
    public String getTaskId() {
        return mBuilder.getTask() == null ?
                mBuilder.getTaskId() : mBuilder.getTask().getTaskId();
    }

    @Override
    public String getGroupId() {
        return mBuilder.getTask() == null ?
                mBuilder.getGroupId() : mBuilder.getTask().getGroupId();
    }

    @Override
    public int getState() {
        return mBuilder.getState();
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
        return mBuilder.getTaskIds();
    }

    @Override
    public ProcessType getProceeType() {
        return mBuilder.getProceeType();
    }

    @Override
    public TaskType getTaskType() {
        return mBuilder.getTask() == null ?
                mBuilder.getTaskType() : mBuilder.getTask().getType();
    }
}
