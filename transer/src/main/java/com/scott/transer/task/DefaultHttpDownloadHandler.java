package com.scott.transer.task;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/16</P>
 * <P>Email: shilec@126.com</p>
 */

public class DefaultHttpDownloadHandler extends BaseTaskHandler {
    @Override
    public boolean isPiceSuccessful() {
        return false;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    @Override
    protected byte[] readPice(Task task) throws IOException {
        return new byte[0];
    }

    @Override
    protected void writePice(byte[] datas, Task task) throws IOException {

    }

    @Override
    protected void prepare(Task task) throws IOException {

    }
}
