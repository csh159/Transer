package com.scott.transer.task;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.http.OkHttpProxy;
import com.scott.transer.utils.Debugger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 11:59</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:
 *  默认的http upload handler
 * </p>
 */

public class DefaultHttpUploadHandler extends BaseTaskHandler {

    private RandomAccessFile mFile;
    private String mResponse;     //返回数据
    private int mPiceRealSize = 0; //每一片的实际大小
    private PiceRequestBody mRequestBody; // 写入一片
    private final String TAG = DefaultHttpUploadHandler.class.getSimpleName();

    @Override
    public boolean isPiceSuccessful() { //判断一片是否成功
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

        try {
            JSONObject job = new JSONObject(mResponse);
            String range = job.optString("range");
            String end = range.split("-")[0];
            String all = range.split("-")[1];

            if(Long.parseLong(end) - 1 >= Long.parseLong(all)) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Debugger.error(TAG,e.getMessage());
        }
        return false;
    }

    @Override
    protected byte[] readPice(Task task) throws IOException{
        byte[] datas = new byte[getPiceBuffSize()];
        mPiceRealSize = mFile.read(datas,0, getPiceBuffSize());
        return datas;
    }

    @Override
    protected void writePice(byte[] datas, Task task) throws IOException{

        //服务端需要支持 Content-Range 的 header
        mRequestBody = new PiceRequestBody(datas);
        Request request = new Request.Builder()
                .addHeader("Session-ID", task.getSesstionId())
                .addHeader("Content-Range", "bytes " + task.getStartOffset()
                        + "-" + (task.getEndOffset() - 1) + "/" + mFile.length())
                .addHeader("Content-Disposition", "attachment; filename=" + task.getName())
                .addHeader("Connection", "Keep-Alive")
                .url(task.getDestSource())
                .post(mRequestBody)
                .build();
        OkHttpClient client = OkHttpProxy.getClient();
        Call call = client.newCall(request);

        mResponse = null;
        Debugger.error(TAG,"wait response === ");
        Response execute = call.execute();
        Debugger.error(TAG,"wait2 response === ");
        if(!execute.isSuccessful()) {
            return;
        }
        ResponseBody body = execute.body();
        mResponse = body.string();
        Debugger.error(TAG,"response === " + mResponse);
    }

    @Override
    protected void release() {
        super.release();
        try {
            mFile.close();
            mRequestBody = null;
            mResponse = null;
            mPiceRealSize = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepare(ITask task) throws IOException{
        mFile = new RandomAccessFile(task.getDataSource(),"r");
        //将文件指针移动到上次传输的位置
        mFile.seek(task.getCompleteLength());
    }

    @Override
    protected int getPiceRealSize() {
        return mPiceRealSize;
    }

    @Override
    protected long fileSize() {
        try {
            return mFile.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected long getCurrentCompleteLength() {
        if(mRequestBody == null) {
            return super.getCurrentCompleteLength();
        }
        return super.getCurrentCompleteLength() + mRequestBody.mCurrentCompleteLength;
    }

    protected class PiceRequestBody extends RequestBody {

        private ByteArrayInputStream mSource; //当前需要传输的一片
        private volatile int mCurrentCompleteLength; //当前已经完成的长度，写入多少增加多少

        PiceRequestBody(byte[] datas) {
            mSource = new ByteArrayInputStream(datas,0,getPiceRealSize());
        }

        @Override
        public long contentLength() throws IOException {
            //需要指定此次请求的内容长度，以从数据圆中实际读取的长度为准
            Debugger.error(TAG,"offset = " + getTask().getStartOffset() + ", end = " + getTask().getEndOffset());
            return getPiceRealSize();
        }

        @Override
        public MediaType contentType() {
            //服务器支持的contenttype 类型
            return MediaType.parse("application/octet-stream");
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            byte[] buf = new byte[8192];
            int len = 0;

            //这里这样处理是由于可以得到进度的连续变化数值，而不需要等到一片传完才等获取已经传输的长度
            while((len = mSource.read(buf)) != -1) {
                sink.write(buf,0,len);
                sink.flush();
                mCurrentCompleteLength += len;
            }

            mSource.reset();
            mSource.close();
        }
    }
}
