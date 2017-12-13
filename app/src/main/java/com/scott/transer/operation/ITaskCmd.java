package com.scott.transer.operation;

import com.scott.transer.task.ITask;
import com.scott.transer.task.TaskType;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 16:10</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskCmd {

    ITask getTask();

    List<ITask> getTasks();

    OperationType getOperationType();

    TaskType getTaskType();
}
