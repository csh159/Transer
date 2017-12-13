package com.scott.transer.operation;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 16:13</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskManager implements ITaskManager {

    private ITaskOperation mOperationProxy;

    @Override
    public void excute(ITaskCmd cmd) {
        switch (cmd.getOperationType()) {
        }
    }

    @Override
    public void setTaskOperation(ITaskOperation operation) {

    }
}
