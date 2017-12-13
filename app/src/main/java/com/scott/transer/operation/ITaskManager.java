package com.scott.transer.operation;

import com.scott.transer.task.ITaskHolderProxy;
import com.scott.transer.task.ITaskStatusCallback;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 14:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

interface ITaskManager {

    void excute(ITaskCmd cmd);

    void setTaskOperation(ITaskOperation operation);

    void setOperationCallback(ITaskOperationCallback callback);

    void addThreadPool(int type, ExecutorService threadPool);

    ExecutorService getThreadPool(int type);

    List<ITaskHolderProxy> getTaskHolders();
}
