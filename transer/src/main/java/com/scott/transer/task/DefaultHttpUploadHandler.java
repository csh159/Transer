package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.http.OkHttpProxy;
import com.scott.transer.http.UploadStreamRequestBody;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Sink;
import okio.Source;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 11:59</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class DefaultHttpUploadHandler extends BaseTaskHandler {

    private RandomAccessFile mFile;
    private final long MAX_PICE_SIZE = 1 * 1024 * 1024;
    private String mResponse;
    private int mPiceRealSize = 0;

    @Override
    public boolean isPiceSuccessful() {
        if(mResponse == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSuccessful() {
        if(mResponse == null) {
            return false;
        }
        return true;
    }

    @Override
    protected byte[] readPice(Task task) throws IOException{
        if (mFile.length() - task.getStartOffset() > MAX_PICE_SIZE) {
            task.setEndOffset(task.getStartOffset() + MAX_PICE_SIZE);
        } else {
            task.setEndOffset(mFile.length());
        }

        byte[] datas = new byte[(int) MAX_PICE_SIZE];
        mPiceRealSize = mFile.read(datas,(int)task.getStartOffset(),(int)task.getEndOffset());
        return datas;
    }

    @Override
    protected void writePice(byte[] datas, Task task) throws IOException{

        RequestBody fileBody = new UploadStreamRequestBody("application/octet-stream",
                mFile,task.getStartOffset(),task.getEndOffset());

        Request request = new Request.Builder()
                .addHeader("Session-ID", task.getSesstionId())
                .addHeader("Content-Range", "bytes " + task.getStartOffset()
                        + "-" + (task.getEndOffset() - 1) + "/" + mFile.length())
                .addHeader("Content-Disposition", "attachment; filename=" + task.getDataSource())
                .addHeader("Connection", "Keep-Alive")
                .url(task.getDestSource())
                .post(fileBody)
                .build();
        OkHttpClient client = OkHttpProxy.getClient();
        Call call = client.newCall(request);

        Response execute = call.execute();
        if(!execute.isSuccessful()) {
            return;
        }
        ResponseBody body = execute.body();
        mResponse = body.string();
    }


    @Override
    protected void prepare(ITask task) throws IOException{
        mFile = new RandomAccessFile(task.getDataSource(),"rw");
        //task.setLength(mFile.length());
        mFile.seek(task.getCompleteLength());
        //task.setStartOffset(task.getCompleteLength());
    }

    @Override
    protected int getPiceRealSize() {
        return mPiceRealSize;
    }

    @Override
    protected long fileSize() {
        return 0;
    }
}
