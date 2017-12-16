package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;

import java.util.concurrent.ExecutorService;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 11:59</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class DefaultUploadHandler extends BaseTaskHandler {

    @Override
    public void handle(String src, String dest) {

    }

    @Override
    public int handlePice(String src, String dest, long start, long end) {
        return 0;
    }

    @Override
    public boolean isPiceSuccessful(String response) {
        return false;
    }

    @Override
    public boolean isSuccessful(String response) {
        return false;
    }
}
