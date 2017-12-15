package com.scott.annotionprocessor;


import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 16:41</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskEventDispatcher {
    void dispatchTasks(TaskType taskType, ProcessType type,List<ITask> taskList);
}
