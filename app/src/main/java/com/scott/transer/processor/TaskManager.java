package com.scott.transer.processor;

import com.scott.transer.task.ITask;
import com.scott.transer.task.ITaskHolder;
import com.scott.transer.task.ITaskHolderProxy;
import com.scott.transer.task.TaskState;
import com.scott.transer.task.TaskType;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 16:13</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskManager implements ITaskManager {

    private ITaskProcessor mOperationProxy;
    private ITaskProcessCallback mCallback;

    @Override
    public void process(ITaskCmd cmd) {
        switch (cmd.getOperationType()) {
            case TYPE_ADD_TASKS:
                mOperationProxy.addTasks(cmd.getTasks());
                mCallback.onFinished();
                break;
            case TYPE_ADD_TASK:
                mOperationProxy.addTask(cmd.getTask());
                mCallback.onFinished();
                break;
            case TYPE_DELETE_TASK:
                mOperationProxy.deleteTask(cmd.getTaskId());
                mCallback.onFinished();
                break;
            case TYPE_DELETE_TASKS_SOME:
                mOperationProxy.deleteTasks(cmd.getTaskIds());
                mCallback.onFinished();
                break;
            case TYPE_DELETE_TASKS_GROUP:
                mOperationProxy.deleteGroup(cmd.getGroupId());
                mCallback.onFinished();
                break;
            case TYPE_DELETE_TASKS_ALL:
                mOperationProxy.deleteAll();
                mCallback.onFinished();
                break;
            case TYPE_DELETE_TASKS_COMPLETED:
                mOperationProxy.deleteCompleted();
                mCallback.onFinished();
                break;
            case TYPE_DELETE_TASKS_STATE:
                mOperationProxy.delete(cmd.getTask().getState());
                mCallback.onFinished();
                break;
            case TYPE_CHANGE_TASK:
                mOperationProxy.changeTasksState(cmd.getState(),cmd.getTaskIds());
                mCallback.onFinished();
                break;
            case TYPE_CHANGE_TASK_GROUP:
                mOperationProxy.changeTasksState(cmd.getState(),cmd.getGroupId());
                mCallback.onFinished();
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
            case TASK_CHANGE_TASK_ALL:
                mOperationProxy.changeAllTasksState(cmd.getState());
                break;
            case TASK_CHANGE_TASK_SOME:
                mOperationProxy.changeTasksState(cmd.getState(),cmd.getTaskIds());
                break;
        }
    }

    @Override
    public void setTaskProcessor(ITaskProcessor operation) {
        mOperationProxy = operation;
    }

    @Override
    public void setProcessCallback(ITaskProcessCallback callback) {
        mCallback = callback;
    }

    @Override
    public void addTaskThreadPool(ExecutorService threadPool) {

    }

    @Override
    public ExecutorService getTaskThreadPool(TaskType type) {
        return null;
    }
}
