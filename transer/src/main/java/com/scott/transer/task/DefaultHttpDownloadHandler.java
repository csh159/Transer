package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.http.OkHttpProxy;
import com.scott.transer.utils.Debugger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
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
    final String TAG = DefaultHttpDownloadHandler.class.getSimpleName();

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
            //mFile = null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Debugger.error(DefaultHttpDownloadHandler.class.getSimpleName(),"====release()==========");
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

        //设置url 参数
        String url = getTask().getDataSource();
        for(String k : getParams().keySet()) {
            if(!url.contains("?")) {
                url += "?" + k + "=" + getParams().get(k);
            } else {
                url = "&" + k + "=" + getParams().get(k);
            }
        }

        String path = task.getName() == null ? task.getDestSource() :
                task.getDestSource() + File.separator + task.getName();
        mFile = new RandomAccessFile(path,"rw");
        mFileSize = getNetSize(url); //从服务端获取文件大小

        //如果本地文件大小和服务端文件大小相同，则不进行重复下载
        if(mFile.length() == mFileSize && mFileSize != 0) {
            stop();
            return;
        }
        //将文件指针移动到末尾
        if(task.getStartOffset() != 0) {
            mFile.seek(task.getStartOffset());
        }

        Debugger.error(DefaultHttpDownloadHandler.class.getSimpleName(),
                "=================== fileLength = " + mFile.length() + "," +
                        task.getStartOffset());
        Request request = new Request.Builder()
                .url(url)
                .header("Range","bytes=" +
                        task.getStartOffset() + "-" + mFileSize)
                .build();
        OkHttpClient client = OkHttpProxy.getClient();
        Call call = client.newCall(request);
        Response response = call.execute();
        if(!response.isSuccessful()) {
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
