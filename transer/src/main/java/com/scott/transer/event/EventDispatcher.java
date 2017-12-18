package com.scott.transer.event;

import android.content.Context;
import android.content.Intent;

import com.scott.annotionprocessor.ITaskEventDispatcher;
import com.scott.annotionprocessor.TaskEventAnnotionProcessor;
import com.scott.annotionprocessor.TaskSubcriberParams;
import com.scott.annotionprocessor.TaskSubscriber;
import com.scott.transer.processor.ITaskCmd;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private List<Object> mScribers = new ArrayList<>();
    private Map<Object,TaskSubcriberParams> mScriberParams = new HashMap<>();

    void regist(Object obj) {
        synchronized (mScribers) {
            if(!mScribers.contains(obj)) {
                mScribers.add(obj);
            }
        }
    }

    void unregist(Object obj) {
        synchronized (mScribers) {
            mScribers.remove(obj);
            mScriberParams.remove(obj);
        }
    }


    EventDispatcher(Context context) {
        mContext = context;
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
        for(Object scriber : mScribers) {
            TaskSubcriberParams param = mScriberParams.get(scriber);
            try {
                if(param == null) {
                    param = new TaskSubcriberParams();
                    String clzzName = scriber.getClass().getName();
                    Class cls = Class.forName(clzzName + TaskEventAnnotionProcessor.CLAZZ_EXT);
                    param.dispatcher = (ITaskEventDispatcher) cls.newInstance();
                    Field mScriber = cls.getField("mScriber");
                    mScriber.setAccessible(true);
                    mScriber.set(param.dispatcher,scriber);
                }
                param.dispatcher.dispatchTasks(taskType,type,taskList);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
