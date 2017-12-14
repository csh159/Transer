package com.scott.transer.processor;

import com.scott.transer.task.ITask;
import com.scott.transer.task.ITaskHolder;
import com.scott.transer.task.ITaskHolderProxy;
import com.scott.transer.task.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 14:42</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskManagerProxy implements ITaskManagerProxy, ITaskProcessCallback{

    private ITaskManager mManager;
    private ITaskProcessor mProcessor;
    private ExecutorService mCmdThreadPool;
    private ITaskProcessCallback mProcessCallback;
    private List<ITaskHolder> mTasks = new ArrayList<>();

    TaskManagerProxy() {
        mCmdThreadPool = Executors.newSingleThreadExecutor();
    }

    @Override
    public void setTaskManager(ITaskManager manager) {
        mManager = manager;
        mManager.setProcessCallback(this);
        mManager.setTaskProcessor(mProcessor);
    }

    @Override
    public void process(final ITaskCmd cmd) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mManager.process(cmd);
            }
        };
        mCmdThreadPool.execute(runnable);
    }

    @Override
    public void setTaskProcessor(ITaskProcessor operation) {
        mProcessor = operation;
        operation.setTaskHolders(mTasks);
    }

    @Override
    public void setProcessCallback(ITaskProcessCallback callback) {
        mProcessCallback = callback;
    }

    @Override
    public void addTaskThreadPool(ExecutorService threadPool) {
        mManager.addTaskThreadPool(threadPool);
    }

    @Override
    public ExecutorService getTaskThreadPool(TaskType type) {
        return mManager.getTaskThreadPool(type);
    }

    @Override
    public void onFinished(List<ITask> tasks) {
        mProcessCallback.onFinished(tasks);
    }

    @Override
    public void onFinished(ITask task) {
        mProcessCallback.onFinished(task);
    }

    @Override
    public void onFinished() {
        mProcessCallback.onFinished();
    }
}
