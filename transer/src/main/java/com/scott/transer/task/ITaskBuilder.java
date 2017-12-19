package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.processor.ITaskCmdBuilder;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/16</P>
 * <P>Email: shilec@126.com</p>
 */

public interface ITaskBuilder extends ITask {

    ITaskBuilder setDataSource(String dataSource);

    ITaskBuilder setDestSource(String destSource);

    ITaskBuilder setSesstionId(String sesstionId);

    ITaskBuilder setLength(long length);

    ITaskBuilder setStartoffset(long offset);

    ITaskBuilder setEndOffset(long offset);

    ITaskBuilder setGroupId(String groupId);

    ITaskBuilder setGroupName(String groupName);

    ITaskBuilder setCompleteTime(long completeTime);

    ITaskBuilder setCompleteLength(long length);

    ITaskBuilder setState(int state);

    ITaskBuilder setTaskType(TaskType type);

    ITaskBuilder setUserId(String userId);

    ITask build();

    ITaskBuilder setTask(ITask task);

    ITaskBuilder setName(String name);

    ITaskBuilder setTaskId(String taskId);

    ITaskBuilder setSessionId(String sesstionId);

    ITaskBuilder setSpeed(long speed);
}
