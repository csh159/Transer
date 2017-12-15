package com.scott.transer.task;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 13:17</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskHolderProxy extends ITaskHolder{

    void setTaskStateListener(ITaskStatusCallback callback);

    void setTaskHandler(ITaskInternalHandler handler);
}
