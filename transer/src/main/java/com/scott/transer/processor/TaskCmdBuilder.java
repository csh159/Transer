package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.processor.ITaskCmd;
import com.scott.transer.processor.ITaskCmdBuilder;
import com.scott.transer.processor.TaskCmd;

import java.util.List;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/16</P>
 * <P>Email: shilec@126.com</p>
 */

public class TaskCmdBuilder implements ITaskCmdBuilder {

    private TaskType taskType = TaskType.TYPE_UPLOAD;
    private ProcessType processType;
    private ITask task;
    private List<ITask> tasks;

    @Override
    public ITaskCmdBuilder setTaskId(String taskId) {
        return null;
    }

    @Override
    public ITaskCmdBuilder setGroupId(String groupId) {
        return null;
    }

    @Override
    public ITaskCmdBuilder setState(int state) {
        return null;
    }

    @Override
    public ITaskCmdBuilder setTask(ITask task) {
        this.task = task;
        return this;
    }

    @Override
    public ITaskCmdBuilder setTasks(List<ITask> tasks) {
        this.tasks = tasks;
        return this;
    }

    @Override
    public ITaskCmdBuilder setTaskIds(String[] taskIds) {
        return null;
    }

    @Override
    public ITaskCmdBuilder setProcessType(ProcessType type) {
        this.processType = type;
        return this;
    }

    @Override
    public ITaskCmdBuilder setTaskType(TaskType type) {
        return null;
    }

    @Override
    public ITaskCmdBuilder setTaskCmd(ITaskCmd cmd) {
        return null;
    }

    @Override
    public ITaskCmd build() {
        ITaskCmd cmd = new TaskCmd(this);
        return cmd;
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
        return task;
    }

    @Override
    public List<ITask> getTasks() {
        return tasks;
    }

    @Override
    public String[] getTaskIds() {
        return new String[0];
    }

    @Override
    public ProcessType getOperationType() {
        return processType;
    }

    @Override
    public TaskType getTaskType() {
        return taskType;
    }
}
