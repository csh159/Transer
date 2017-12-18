package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 17:51</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

@Entity
public class Task implements ITask {

    private String dataSource;
    private String destSource;
    private String sesstionId;
    private long length;
    private long startOffset;
    private long endOffset;
    private String taskId;
    private String groupId;
    private String groupName;
    private long completeTime;
    private long completeLength;
    private int state;
    private String name;

    public static final class TaskTypeConverter implements PropertyConverter<TaskType,Integer> {

        @Override
        public TaskType convertToEntityProperty(Integer databaseValue) {
            TaskType taskType = TaskType.TYPE_UPLOAD;
            switch (databaseValue) {
                case 0:
                    taskType = TaskType.TYPE_UPLOAD;
                    break;
                case 1:
                    taskType = TaskType.TYPE_DOWNLOAD;
                    break;
            }
            return taskType;
        }

        @Override
        public Integer convertToDatabaseValue(TaskType entityProperty) {
            int value = 0;
            switch (entityProperty) {
                case TYPE_DOWNLOAD:
                    value = 1;
                    break;
                case TYPE_UPLOAD:
                    value = 1;
                    break;
            }
            return value;
        }
    }

    @Convert(converter = TaskTypeConverter.class,columnType = Integer.class)
    private TaskType type;
    private String userId;


    public Task(ITaskBuilder builder) {
        this.dataSource = builder.getDataSource();
        this.destSource = builder.getDestSource();
        this.sesstionId = builder.getSesstionId();
        this.length = builder.getLength();
        this.startOffset = builder.getStartOffset();
        this.endOffset = builder.getEndOffset();
        this.taskId = builder.getTaskId();
        this.groupId = builder.getGroupId();
        this.groupName = builder.getGroupName();
        this.completeTime = builder.getCompleteTime();
        this.completeLength = builder.getCompleteLength();
        this.state = builder.getState();
        this.type = builder.getType();
        this.userId = builder.getUserId();
        this.name = builder.getName();
    }

    @Generated(hash = 420318931)
    public Task(String dataSource, String destSource, String sesstionId, long length, long startOffset,
            long endOffset, String taskId, String groupId, String groupName, long completeTime,
            long completeLength, int state, String name, TaskType type, String userId) {
        this.dataSource = dataSource;
        this.destSource = destSource;
        this.sesstionId = sesstionId;
        this.length = length;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.taskId = taskId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.completeTime = completeTime;
        this.completeLength = completeLength;
        this.state = state;
        this.name = name;
        this.type = type;
        this.userId = userId;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

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
        return sesstionId;
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
        return taskId;
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
        return type;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public void setDestSource(String destSource) {
        this.destSource = destSource;
    }

    public void setSesstionId(String sesstionId) {
        this.sesstionId = sesstionId;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setStartOffset(long startOffset) {
        this.startOffset = startOffset;
    }

    public void setEndOffset(long endOffset) {
        this.endOffset = endOffset;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public void setCompleteLength(long completeLength) {
        this.completeLength = completeLength;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "dataSource='" + dataSource + '\'' +
                ", destSource='" + destSource + '\'' +
                ", sesstionId='" + sesstionId + '\'' +
                ", length=" + length +
                ", startOffset=" + startOffset +
                ", endOffset=" + endOffset +
                ", taskId='" + taskId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", completeTime=" + completeTime +
                ", completeLength=" + completeLength +
                ", state=" + state +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
