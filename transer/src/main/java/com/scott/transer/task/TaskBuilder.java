package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.task.ITaskBuilder;
import com.scott.transer.task.Task;

import java.util.List;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/16</P>
 * <P>Email: shilec@126.com</p>
 */

public class TaskBuilder implements ITaskBuilder {

    private String dataSource;

    private String destSource;

    private long length;

    private long startOffset;

    private long endOffset;

    private String tasksId;

    private String sessionId;

    private String groupId;

    private String groupName;

    private long completeTime;

    private long completeLength;

    private int state;

    private String userId;

    private String name;

    private TaskType taskType = TaskType.TYPE_UPLOAD;

    private ITask task;

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
        return sessionId;
    }

    @Override
    public long getLength() {
        return length;
    }

    @Override
    public long getStartOffset() {
        return startOffset;
    }

    @Override
    public long getEndOffset() {
        return endOffset;
    }

    @Override
    public String getTaskId() {
        return tasksId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public long getCompleteTime() {
        return completeTime;
    }

    @Override
    public long getCompleteLength() {
        return completeLength;
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public TaskType getType() {
        return taskType;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return name;
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
        this.sessionId = sesstionId;
        return this;
    }

    @Override
    public ITaskBuilder setLength(long length) {
        this.length = length;
        return this;
    }

    @Override
    public ITaskBuilder setStartoffset(long offset) {
        this.startOffset = offset;
        return this;
    }

    @Override
    public ITaskBuilder setEndOffset(long offset) {
        this.endOffset = offset;
        return this;
    }

    @Override
    public ITaskBuilder setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    @Override
    public ITaskBuilder setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    @Override
    public ITaskBuilder setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
        return this;
    }

    @Override
    public ITaskBuilder setCompleteLength(long length) {
        this.completeLength = length;
        return this;
    }

    @Override
    public ITaskBuilder setState(int state) {
        this.state = state;
        return this;
    }

    @Override
    public ITaskBuilder setTaskType(TaskType type) {
        taskType = type;
        return this;
    }

    @Override
    public ITaskBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public ITask build() {
        if(task != null) {
            return task;
        }
        task = new Task(this);
        return task;
    }

    @Override
    public ITaskBuilder setTask(ITask task) {
        this.task = task;
        return this;
    }

    @Override
    public ITaskBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ITaskBuilder setTaskId(String taskId) {
        this.tasksId = taskId;
        return this;
    }

    @Override
    public ITaskBuilder setSessionId(String sesstionId) {
        this.sessionId = sesstionId;
        return this;
    }
}
