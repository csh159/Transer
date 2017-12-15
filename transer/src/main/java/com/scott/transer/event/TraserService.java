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

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 15:45</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TraserService extends Service implements ITaskProcessCallback{

    ITaskManagerProxy mTaskManagerProxy;
    ITaskManager mTaskManager;
    ITaskProcessor mTaskProcessor;
    EventDispatcher mDispatcher;
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
                ITaskCmd cmd = mDispatcher.getTaskCmd();
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
        mTaskManager = new TaskManager();
        mTaskProcessor = new ProcessorProxy(new TaskProcessor(),new TaskDbProcessor());

        mTaskManagerProxy.setTaskManager(mTaskManager);
        mTaskManagerProxy.setProcessCallback(this);
        mTaskManagerProxy.setTaskProcessor(mTaskProcessor);

        mDispatcher = new EventDispatcher(getApplicationContext());
    }

    @Override
    public void onFinished(TaskType taskType, ProcessType type,List<ITask> tasks) {
        mDispatcher.dispatchTasks(taskType,type,tasks);
    }
}
