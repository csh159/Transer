package com.scott.transer.task;

import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 11:52</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskInternalHandler extends ITaskHolder{

    void handle(String src,String dest);

    int handlePice(String src,String dest,long start,long end);

    boolean isPiceSuccessful(String response);

    boolean isSuccessful(String response);

    void setThreadPool(ExecutorService threadPool);

    void start();

    void stop();

    void pause();

    void resume();
}
