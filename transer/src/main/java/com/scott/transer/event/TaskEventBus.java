package com.scott.transer.event;

import com.scott.transer.processor.ITaskCmd;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-14 16:05</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskEventBus {

    public void regesit(Object obj) {

    }

    public void unregesit(Object obj) {

    }

    public static TaskEventBus getDefault() {
        return null;
    }

    public synchronized void post(ITaskCmd cmd) {
        ICmdEventDispatcher dispatcher = EventDispatcher.getCmdDispatcher();
        if(dispatcher == null) {
            return;
        }
        dispatcher.dispatchCmd(cmd);
    }
}
