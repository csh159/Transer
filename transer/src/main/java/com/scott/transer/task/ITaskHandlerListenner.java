package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 12:47</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskHandlerListenner {

    void onStart(ITask params);

    void onStop(ITask params);

    void onError(int code,ITask params);

    void onSpeedChanged(long speed,ITask params);

    void onPiceSuccessful(ITask params);

    void onResume(ITask params);

    void onPause(ITask params);

    void onFinished(ITask task);

}
