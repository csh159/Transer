package com.scott.transer.event;

import android.content.Context;
import android.content.Intent;

import com.scott.annotionprocessor.ITaskEventDispatcher;
import com.scott.transer.processor.ITaskCmd;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 16:27</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

class EventDispatcher implements ICmdEventDispatcher,ITaskEventDispatcher {

    private Queue<ITaskCmd> mCmdQueue = new ArrayDeque<>();
    private Context mContext;
    private static ICmdEventDispatcher mCmdDispatcher;

    static ICmdEventDispatcher getCmdDispatcher() {
        return mCmdDispatcher;
    }

    EventDispatcher(Context context) {
        mContext = context;
        mCmdDispatcher = this;
    }

    @Override
    public void dispatchCmd(ITaskCmd cmd) {
        synchronized (mCmdQueue) {
            mCmdQueue.add(cmd);
            Intent intent = new Intent(mContext, TraserService.class);
            intent.setAction(TraserService.ACTION_EXECUTE_CMD);
            mContext.startService(intent);
        }
    }

    synchronized ITaskCmd getTaskCmd() {
        return mCmdQueue.poll();
    }

    @Override
    public void dispatchTasks(TaskType taskType, ProcessType type, List<ITask> taskList) {

    }
}
