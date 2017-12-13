package com.scott.transer.task;

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

    Runnable toRunnable();
}
