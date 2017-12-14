package com.scott.transer.processor;

import com.scott.transer.task.ITaskHolder;
import com.scott.transer.task.ITaskHolderProxy;
import com.scott.transer.task.TaskType;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 14:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

interface ITaskManager {

    void process(ITaskCmd cmd);

    void setTaskProcessor(ITaskProcessor operation);

    void setProcessCallback(ITaskProcessCallback callback);

    void addTaskThreadPool(ExecutorService threadPool);

    ExecutorService getTaskThreadPool(TaskType type);
}
