package com.scott.transer.event;

import android.os.Handler;
import android.os.Message;

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

class MainTaskPoster implements ITaskPoster {

    private ArrayBlockingQueue<ITaskEventDispatcher> mTaskQueue =
            new ArrayBlockingQueue<ITaskEventDispatcher>(10);
    private Handler mMainHandler = new Handler();

    MainTaskPoster() {

    }

    @Override
    public void enqueue(ITaskEventDispatcher dispatcher,TaskType taskType, ProcessType type, List<ITask> taskList) {
        mTaskQueue.add(dispatcher);
        synchronized (mTaskQueue) {
            while(!mTaskQueue.isEmpty()) {
                ITaskEventDispatcher dispatcher1 = mTaskQueue.poll();
                post(dispatcher1,taskType,type,taskList);
            }
        }
    }

    private void post(final ITaskEventDispatcher dispatcher, final TaskType taskType,
                      final ProcessType type, final List<ITask> taskList) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                dispatcher.dispatchTasks(taskType,type,taskList);
            }
        });
    }
}
