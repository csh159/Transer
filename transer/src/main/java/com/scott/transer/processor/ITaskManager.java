package com.scott.transer.processor;

import com.scott.annotionprocessor.TaskType;
import com.scott.transer.task.ITaskInternalHandler;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 14:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskManager {

    void process(ITaskCmd cmd);

    void setTaskProcessor(ITaskProcessor operation);

    void setProcessCallback(ITaskProcessCallback callback);

    void setThreadPool(TaskType taskType,ExecutorService threadPool);

    ExecutorService getTaskThreadPool(TaskType type);

    ITaskInternalHandler getTaskHandler(TaskType taskType);

    void setTaskHandler(TaskType type,Class<? extends ITaskInternalHandler> handler);

    void setHeaders(Map<String,String> headers);

    void setParams(Map<String,String> params);
}
