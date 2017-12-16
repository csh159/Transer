package com.scott.transer.task;

import java.util.concurrent.ExecutorService;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/16</P>
 * <P>Email: shilec@126.com</p>
 */

public class DefaultDownloadHandler extends BaseTaskHandler {
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
