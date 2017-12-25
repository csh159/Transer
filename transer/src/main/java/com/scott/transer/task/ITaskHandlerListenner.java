package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 12:47</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskHandlerListenner {

    void onStart(ITask task);

    void onStop(ITask task);

    void onError(int code,ITask task);

    void onSpeedChanged(long speed,ITask task);

    void onPiceSuccessful(ITask task);

    void onFinished(ITask task);

}
