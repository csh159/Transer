package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;

import java.io.IOError;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 11:52</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskHandler extends ITaskHolder{

    void setThreadPool(ExecutorService threadPool);

    void start();

    void stop();

    void pause();

    void resume();

    Map<String,String> getHeaders();

    Map<String,String> getParams();

    void setHeaders(Map<String,String> headers);

    void setParams(Map<String,String> params);

    void setHandlerListenner(ITaskHandlerListenner l);
}
