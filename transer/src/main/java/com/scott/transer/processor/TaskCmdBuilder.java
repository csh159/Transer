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

    private TaskType taskType;
    private ProcessType processType;
    private ITask task;
    private List<ITask> tasks;
    private ITaskCmd cmd;
    private String[] taskids;
    private String taskId;
    private int state;
    private String groupId;

    @Override
    public ITaskCmdBuilder setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    @Override
    public ITaskCmdBuilder setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    @Override
    public ITaskCmdBuilder setState(int state) {
        this.state = state;
        return this;
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
        this.taskids = taskIds;
        return this;
    }

    @Override
    public ITaskCmdBuilder setProcessType(ProcessType type) {
        this.processType = type;
        return this;
    }

    @Override
    public ITaskCmdBuilder setTaskType(TaskType type) {
        taskType = type;
        return this;
    }

    @Override
    public ITaskCmdBuilder setTaskCmd(ITaskCmd cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public ITaskCmd build() {
        if(cmd != null) {
            return cmd;
        }
        ITaskCmd cmd = new TaskCmd(this);
        return cmd;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public int getState() {
        return state;
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
        return taskids;
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
