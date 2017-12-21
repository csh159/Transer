package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.http.OkHttpProxy;
import com.scott.transer.utils.Debugger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.Source;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/16</P>
 * <P>Email: shilec@126.com</p>
 */

public class DefaultHttpDownloadHandler extends BaseTaskHandler {

    private RandomAccessFile mFile;
    private InputStream mInputStream;
    private final int MAX_PICE_SIZE = 1 * 1024 * 1024;
    private int mPiceSize = 0;
    private long mFileSize = 0;

    @Override
    public boolean isPiceSuccessful() {
        return true;
    }

    @Override
    public boolean isSuccessful() {
        return getTask().getLength() == getTask().getCompleteLength();
    }

    @Override
    protected void release() {
        super.release();
        try {
            mFile.close();
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected byte[] readPice(Task task) throws IOException {
        if(mInputStream == null) {
            return null;
        }
        byte[] buf = new byte[MAX_PICE_SIZE];
        mPiceSize = mInputStream.read(buf);
        return buf;
    }

    @Override
    protected void writePice(byte[] datas,Task task) throws IOException {
        mFile.write(datas,0,getPiceRealSize());
    }

    @Override
    protected void prepare(ITask task) throws Exception {

        String path = task.getName() == null ? task.getDestSource() :
                task.getDestSource() + File.separator + task.getName();
        mFile = new RandomAccessFile(path,"rw");
        mFileSize = getNetSize(task.getDataSource());

        if(mFile.length() == mFileSize && mFileSize != 0) {
            stop();
            return;
        }
        Debugger.info(DefaultHttpDownloadHandler.class.getSimpleName(),
                "=================== fileLength = " + mFile.length() + "," +
                        task.getStartOffset());

        if(task.getStartOffset() != 0) {
            mFile.seek(task.getStartOffset());
        }

        Request request = new Request.Builder()
                .url(task.getDataSource())
                .header("Range","bytes=" +
                        task.getStartOffset() + "-" + mFileSize)
                .build();
        Debugger.error(DefaultHttpDownloadHandler.class.getSimpleName()," ====== startOffset = " + task.getStartOffset()
                + ", fileSize = " + mFileSize);
        OkHttpClient client = OkHttpProxy.getClient();
        Call call = client.newCall(request);
        Response response = call.execute();
        if(!response.isSuccessful()) {
            mListenner.onError(TaskErrorCode.ERROR_CODE_NETWORK,task);
            stop();
            return;
        }
        ResponseBody body = response.body();
        mInputStream = body.byteStream();
    }

    @Override
    protected int getPiceRealSize() {
        return mPiceSize;
    }

    @Override
    protected long fileSize() {
        return mFileSize;
    }

    private long getNetSize(String src) throws Exception {
        Request request = new Request.Builder()
                .url(src)
                .head()
                .build();
        OkHttpClient client = OkHttpProxy.getClient();
        Call call = client.newCall(request);

        Response response = call.execute();
        String header = response.header("Content-Length");
        long length = Long.parseLong(header);

        return length;
    }
}
