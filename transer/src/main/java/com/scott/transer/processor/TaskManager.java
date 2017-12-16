package com.scott.transer.processor;

import android.util.Log;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.task.ITaskInternalHandler;
import com.scott.transer.task.TaskState;
import com.scott.annotionprocessor.TaskType;

import java.util.HashMap;
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

    private ITaskProcessor mOperationProxy;
    private ITaskProcessCallback mCallback;
    private Map<TaskType,ExecutorService> mThreadPool = new HashMap<>();
    private Map<TaskType,Class<? extends ITaskInternalHandler>> mTaskHandlers = new HashMap<>();

    @Override
    public void process(ITaskCmd cmd) {
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
                mOperationProxy.getTask(cmd.getTaskId());
                break;
            case TYPE_QUERY_TASKS_SOME:
                mOperationProxy.getTasks(cmd.getTaskIds());
                break;
            case TYPE_QUERY_TASKS_ALL:
                mOperationProxy.getAllTasks();
                break;
            case TYPE_QUERY_TASKS_COMPLETED:
                mOperationProxy.getTasks(TaskState.STATE_FINISH);
                break;
            case TYPE_QUERY_TASKS_GROUP:
                mOperationProxy.getGroup(cmd.getGroupId());
                break;
            case TYPE_QUERY_TASKS_STATE:
                mOperationProxy.getTasks(cmd.getState());
                break;
            case TASK_CHANGE_TASK_ALL:
                mOperationProxy.changeAllTasksState(cmd.getState());
                break;
            case TASK_CHANGE_TASK_SOME:
                mOperationProxy.changeTasksState(cmd.getState(),cmd.getTaskIds());
                break;
        }
        mCallback.onFinished(cmd.getTaskType(),cmd.getOperationType(),null);
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
    public void setThreadPool(TaskType taskType, ExecutorService threadPool) {
        mThreadPool.put(taskType,threadPool);
    }

    @Override
    public ExecutorService getTaskThreadPool(TaskType type) {
        return mThreadPool.get(type);
    }

    @Override
    public ITaskInternalHandler getTaskHandler(TaskType taskType) {
        Class<? extends ITaskInternalHandler> handlerClzz = mTaskHandlers.get(taskType);
        try {
            ITaskInternalHandler taskHandler = handlerClzz.newInstance();
            taskHandler.setThreadPool(getTaskThreadPool(taskType));
            return taskHandler;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setTaskHandler(TaskType type, Class<? extends ITaskInternalHandler> handler) {
        mTaskHandlers.put(type,handler);
    }

}
