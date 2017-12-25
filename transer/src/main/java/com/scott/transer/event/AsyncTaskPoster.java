package com.scott.transer.event;

import android.os.Handler;
import android.os.Looper;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.ITaskEventDispatcher;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskType;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/25</P>
 * <P>Email: shilec@126.com</p>
 */
class AsyncTaskPoster implements ITaskPoster,Runnable{
    private Thread mAsyncThread;
    private Handler mAsyncHandler;
    private ArrayBlockingQueue<ITaskEventDispatcher> mTaskQueue = new ArrayBlockingQueue<ITaskEventDispatcher>(10);

    AsyncTaskPoster() {
        mAsyncThread = new Thread(this);
        mAsyncThread.setName("AsyncTaskPoster");
        mAsyncThread.start();
    }

    @Override
    public void enqueue(ITaskEventDispatcher dispatcher, final TaskType taskType, final ProcessType type, final List<ITask> taskList) {
        mTaskQueue.add(dispatcher);
        synchronized (mTaskQueue) {
            while (!mTaskQueue.isEmpty()) {
                final ITaskEventDispatcher dispatcher1 = mTaskQueue.poll();
                mAsyncHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dispatcher1.dispatchTasks(taskType,type,taskList);
                    }
                });
            }
        }
    }

    @Override
    public void run() {
        Looper.prepare();
        mAsyncHandler = new Handler();
        Looper.loop();
    }
}
