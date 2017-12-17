package com.scott.transer.http;



import java.io.IOException;
import java.io.RandomAccessFile;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/8/15.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class UploadStreamRequestBody extends RequestBody{

    private String mContentType;
    private RandomAccessFile mFile;
    private long end;
    private long start;
    //private ITaskChangeCallback callback;
    //private ITaskRequest.Options options;

    public UploadStreamRequestBody(String mContentType, RandomAccessFile file,
                                   long start, long end) {
        this.mContentType = mContentType;
        mFile = file;
        this.start = start;
        this.end = end;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(mContentType);
    }

    @Override
    public long contentLength() throws IOException {
        return end - start;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        //byte[] buf = new byte[options.maxBuffSize];
//        int len = 0;
//        int allLen = 0;
//
//        while((len = mFile.read(buf)) != -1) {
//
//            sink.write(buf,0,len);
//            sink.flush();
//            allLen += len;
//            //options.taskInfo.compeleteLength += len;
//            if(allLen >= (end - start)) {
//                break;
//            }
//        }

    }
}
