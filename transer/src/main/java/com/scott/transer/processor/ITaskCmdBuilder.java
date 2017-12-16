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

public interface ITaskCmdBuilder extends ITaskCmd{

    ITaskCmdBuilder setTaskId(String taskId);

    ITaskCmdBuilder setGroupId(String groupId);

    ITaskCmdBuilder setState(int state);

    ITaskCmdBuilder setTask(ITask task);

    ITaskCmdBuilder setTasks(List<ITask> tasks);

    ITaskCmdBuilder setTaskIds(String[] taskIds);

    ITaskCmdBuilder setProcessType(ProcessType type);

    ITaskCmdBuilder setTaskType(TaskType type);

    ITaskCmdBuilder setTaskCmd(ITaskCmd cmd);

    ITaskCmd build();
}
