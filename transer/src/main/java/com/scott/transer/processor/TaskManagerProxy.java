package com.scott.transer.processor;

import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.ITask;
import com.scott.transer.task.ITaskHandlerHolder;
import com.scott.transer.task.ITaskHandlerListenner;
import com.scott.transer.task.ITaskHolder;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.task.ITaskHandler;
import com.scott.transer.task.TaskState;
import com.scott.transer.utils.Debugger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 14:42</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskManagerProxy implements ITaskManagerProxy, ITaskProcessCallback,ITaskHandlerListenner{

    private ITaskManager mManager;
    private ITaskProcessor mProcessor; //processor proxy
    private ExecutorService mCmdThreadPool; //cmd thread pool
    private ITaskProcessCallback mProcessCallback;
    private final String TAG = TaskManagerProxy.class.getSimpleName();

    public TaskManagerProxy() {
        mCmdThreadPool = Executors.newSingleThreadExecutor();
    }

    @Override
    public void setTaskManager(ITaskManager manager) {
        mManager = manager;
        mManager.setProcessCallback(this);
        mManager.setTaskProcessor(mProcessor);
        mProcessor.setTaskManager(this);
    }

    @Override
    public void process(final ITaskCmd cmd) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (mProcessor) {
                    mManager.process(cmd);
                }
            }
        };
        mCmdThreadPool.execute(runnable);
    }

    @Override
    public void setTaskProcessor(ITaskProcessor operation) {
        mProcessor = operation;
    }

    @Override
    public void setProcessCallback(ITaskProcessCallback callback) {
        mProcessCallback = callback;
    }

    @Override
    public void setThreadPool(TaskType taskType, ThreadPoolExecutor threadPool) {
        mManager.setThreadPool(taskType,threadPool);
    }


    @Override
    public ThreadPoolExecutor getTaskThreadPool(TaskType type) {
        return mManager.getTaskThreadPool(type);
    }

    @Override
    public ITaskHandler getTaskHandler(TaskType taskType) {
        ITaskHandler taskHandler = mManager.getTaskHandler(taskType);
        taskHandler.setHandlerListenner(this);
        return taskHandler;
    }

    @Override
    public void setTaskHandler(TaskType type, Class<? extends ITaskHandler> handler) {
        mManager.setTaskHandler(type,handler);
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        mManager.setHeaders(headers);
    }

    @Override
    public void setParams(Map<String, String> params) {
        mManager.setParams(params);
    }

    @Override
    public List<ITaskHolder> getTasks() {
        return mManager.getTasks();
    }


    @Override
    public void onFinished(TaskType taskType, ProcessType processType, List<ITask> tasks) {
        List<ITask> taskList = new ArrayList<>();
        for(ITaskHolder holder : mManager.getTasks()) {
            if(taskType == holder.getType()) {
                taskList.add(holder.getTask());
            }
        }

        //copy
//        ITask[] objects = (ITask[]) taskList.toArray();
//        ITask[] objects1 = Arrays.copyOf(objects, objects.length);
//        tasks = Arrays.asList(objects1);
        mProcessCallback.onFinished(taskType,processType,taskList);
    }

    @Override
    public void onStart(ITask task) {
        //Debugger.error(TAG,"start = " + params);
        ITaskCmd cmd = new TaskCmdBuilder()
                .setTask(task)
                .setProcessType(ProcessType.TYPE_UPDATE_TASK)
                .build();
        process(cmd);
    }

    @Override
    public void onStop(ITask task) {
        //Debugger.error(TAG,"stop = " + params);
        ITaskCmd cmd = new TaskCmdBuilder()
                .setTask(task)
                .setProcessType(ProcessType.TYPE_UPDATE_TASK)
                .build();
        process(cmd);
    }

    @Override
    public void onError(int code, ITask task) {
        //Debugger.error(TAG,"error = " + params);
        ITaskCmd cmd = new TaskCmdBuilder()
                .setTask(task)
                .setState(TaskState.STATE_STOP)
                .setProcessType(ProcessType.TYPE_UPDATE_TASK)
                .build();
        process(cmd);
    }

    @Override
    public void onSpeedChanged(long speed, ITask task) {
        ITaskCmd cmd = new TaskCmdBuilder()
                .setTask(task)
                .setProcessType(ProcessType.TYPE_UPDATE_TASK_WTIHOUT_SAVE)
                .setState(TaskState.STATE_RUNNING)
                .build();
        process(cmd);
        Debugger.error(TAG,"speed == " + task.getSpeed());
    }

    @Override
    public void onPiceSuccessful(ITask task) {
        //Debugger.error(TAG,"picesuccessful = " + params);
        ITaskCmd cmd = new TaskCmdBuilder()
                .setTask(task)
                .setProcessType(ProcessType.TYPE_UPDATE_TASK)
                .setState(TaskState.STATE_RUNNING)
                .build();
        process(cmd);
    }

    @Override
    public void onFinished(ITask task) {
        //Debugger.error(TAG,"finished = " + task);
        ITaskCmd cmd = new TaskCmdBuilder()
                .setTask(task)
                .setProcessType(ProcessType.TYPE_UPDATE_TASK)
                .setState(TaskState.STATE_FINISH)
                .build();
        process(cmd);
    }

}
