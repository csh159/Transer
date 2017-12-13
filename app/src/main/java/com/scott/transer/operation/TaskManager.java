package com.scott.transer.operation;

import com.scott.transer.task.ITask;
import com.scott.transer.task.ITaskHolderProxy;
import com.scott.transer.task.TaskState;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 16:13</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskManager implements ITaskManager {

    private ITaskOperation mOperationProxy;
    private ITaskOperationCallback mCallback;
    private List<ITaskHolderProxy> mTaskHolders;
    private Map<Integer,ExecutorService> mThreadPools;

    @Override
    public void excute(ITaskCmd cmd) {
       dispatchOperation(cmd);
    }

    private void dispatchOperation(ITaskCmd cmd) {
        switch (cmd.getOperationType()) {
            case TYPE_ADD_TASKS:
                mOperationProxy.addTasks(cmd.getTasks());
                break;
            case TYPE_ADD_TASK:
                mOperationProxy.addTask(cmd.getTask());
                break;
            case TYPE_DELETE_TASK:
                mOperationProxy.deleteTask(cmd.getTaskId());
                break;
            case TYPE_DELETE_TASKS_SOME:
                mOperationProxy.deleteTasks(cmd.getTaskIds());
                break;
            case TYPE_DELETE_TASKS_GROUP:
                mOperationProxy.deleteGroup(cmd.getGroupId());
                break;
            case TYPE_DELETE_TASKS_ALL:
                mOperationProxy.deleteAll();
                break;
            case TYPE_DELETE_TASKS_COMPLETED:
                mOperationProxy.deleteCompleted();
                break;
            case TYPE_DELETE_TASKS_STATE:
                mOperationProxy.delete(cmd.getTask().getState());
                break;
            case TYPE_CHANGE_TASK:
                mOperationProxy.changeTasksState(cmd.getState(),cmd.getTaskIds());
                break;
            case TYPE_CHANGE_TASK_GROUP:
                mOperationProxy.changeTasksState(cmd.getState(),cmd.getGroupId());
                break;
            case TYPE_QUERY_TASK:
                ITask task = mOperationProxy.getTask(cmd.getTaskId());
                mCallback.onFinished(task);
                break;
            case TYPE_QUERY_TASKS_SOME:
                List<ITask> tasks = mOperationProxy.getTasks(cmd.getTaskIds());
                mCallback.onFinished(tasks);
                break;
            case TYPE_QUERY_TASKS_ALL:
                tasks = mOperationProxy.getAllTasks();
                mCallback.onFinished(tasks);
                break;
            case TYPE_QUERY_TASKS_COMPLETED:
                tasks = mOperationProxy.getTasks(TaskState.STATE_FINISH);
                mCallback.onFinished(tasks);
                break;
            case TYPE_QUERY_TASKS_GROUP:
                tasks = mOperationProxy.getGroup(cmd.getGroupId());
                mCallback.onFinished(tasks);
                break;
            case TYPE_QUERY_TASKS_STATE:
                tasks = mOperationProxy.getTasks(cmd.getState());
                mCallback.onFinished(tasks);
                break;
        }
    }

    @Override
    public void setTaskOperation(ITaskOperation operation) {

    }

    @Override
    public void setOperationCallback(ITaskOperationCallback callback) {

    }

    @Override
    public void addThreadPool(int state, ExecutorService threadPool) {

    }

    @Override
    public ExecutorService getThreadPool(int type) {
        return null;
    }

    @Override
    public List<ITaskHolderProxy> getTaskHolders() {
        return null;
    }
}
