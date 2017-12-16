package com.scott.transer.event;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.scott.transer.processor.ITaskCmd;
import com.scott.transer.processor.ITaskManager;
import com.scott.transer.processor.ITaskManagerProxy;
import com.scott.transer.processor.ITaskProcessCallback;
import com.scott.transer.processor.ITaskProcessor;
import com.scott.annotionprocessor.ProcessType;
import com.scott.transer.processor.ProcessorProxy;
import com.scott.transer.processor.TaskDbProcessor;
import com.scott.transer.processor.TaskManager;
import com.scott.transer.processor.TaskManagerProxy;
import com.scott.transer.processor.TaskProcessor;
import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.task.DefaultDownloadHandler;
import com.scott.transer.task.DefaultUploadHandler;
import com.scott.transer.task.Task;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:45</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TraserService extends Service implements ITaskProcessCallback{

    ITaskManagerProxy mTaskManagerProxy;

    public static final String ACTION_EXECUTE_CMD = "_CMD";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_EXECUTE_CMD:
                ITaskCmd cmd = TaskEventBus.sInstance.mDispatcher.getTaskCmd();
                if(cmd != null) {
                    mTaskManagerProxy.process(cmd);
                }
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTaskManagerProxy = new TaskManagerProxy();
        mTaskManagerProxy.setProcessCallback(this);
        mTaskManagerProxy.setTaskProcessor(new ProcessorProxy(new TaskProcessor(),new TaskDbProcessor()));
        mTaskManagerProxy.setTaskManager(new TaskManager());
        mTaskManagerProxy.setTaskHandler(TaskType.TYPE_DOWNLOAD, DefaultDownloadHandler.class);
        mTaskManagerProxy.setTaskHandler(TaskType.TYPE_UPLOAD, DefaultUploadHandler.class);
        mTaskManagerProxy.setThreadPool(TaskType.TYPE_UPLOAD, Executors.newFixedThreadPool(3));
        mTaskManagerProxy.setThreadPool(TaskType.TYPE_DOWNLOAD,Executors.newFixedThreadPool(3));
    }

    @Override
    public void onFinished(TaskType taskType, ProcessType type,List<ITask> tasks) {
        TaskEventBus.sInstance.mDispatcher.dispatchTasks(taskType,type,tasks);
    }
}
