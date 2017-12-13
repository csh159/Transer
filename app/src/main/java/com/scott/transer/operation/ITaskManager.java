package com.scott.transer.operation;

import com.scott.transer.task.ITaskStatusCallback;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 14:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

interface ITaskManager {

    void excute(ITaskCmd cmd);

    void setTaskOperation(ITaskOperation operation);
}
