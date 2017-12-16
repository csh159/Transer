package com.scott.example;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.task.ITaskBuilder;
import com.scott.transer.task.Task;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/16</P>
 * <P>Email: shilec@126.com</p>
 */

public class TaskBuilder implements ITaskBuilder {

    private String dataSource;

    private String destSource;

    private TaskType taskType = TaskType.TYPE_UPLOAD;

    @Override
    public String getDataSource() {
        return dataSource;
    }

    @Override
    public String getDestSource() {
        return destSource;
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
        return taskType;
    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public ITaskBuilder setDataSource(String dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public ITaskBuilder setDestSource(String destSource) {
        this.destSource = destSource;
        return this;
    }

    @Override
    public ITaskBuilder setSesstionId(String sesstionId) {
        return null;
    }

    @Override
    public ITaskBuilder setLength(long length) {
        return null;
    }

    @Override
    public ITaskBuilder setStartoffset(long offset) {
        return null;
    }

    @Override
    public ITaskBuilder setEndOffset(long offset) {
        return null;
    }

    @Override
    public ITaskBuilder setGroupId(String groupId) {
        return null;
    }

    @Override
    public ITaskBuilder setGroupName(String groupName) {
        return null;
    }

    @Override
    public ITaskBuilder setCompleteTime(String completeTime) {
        return null;
    }

    @Override
    public ITaskBuilder setCompleteLength(long length) {
        return null;
    }

    @Override
    public ITaskBuilder setState(int state) {
        return null;
    }

    @Override
    public ITaskBuilder setTaskType(TaskType type) {
        return null;
    }

    @Override
    public ITaskBuilder setUserId(String userId) {
        return null;
    }

    @Override
    public ITask build() {
        ITask task = new Task(this);
        return task;
    }

    @Override
    public ITaskBuilder setTask(ITask task) {
        return null;
    }
}
