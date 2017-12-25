package com.scott.example;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scott.annotionprocessor.ITask;
import com.scott.example.utils.Contacts;
import com.scott.transer.task.TaskBuilder;
import com.scott.transer.task.DefaultHttpUploadHandler;
import com.scott.transer.task.ITaskHandler;
import com.scott.transer.task.SimpleTaskHandlerListenner;
import com.scott.transer.utils.Debugger;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleUploadActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_complete_length)
    TextView tvCompleteLength;

    @BindView(R.id.tv_all_length)
    TextView tvAllLength;

    @BindView(R.id.progress_length)
    ProgressBar progressLength;

    @BindView(R.id.tv_md5)
    TextView tvMd5;

    @BindView(R.id.tv_md5_new)
    TextView tvNewMd5;

    @BindView(R.id.tv_equals)
    TextView tvEquals;

    private ITaskHandler mHandler;

    final String URL = "http://" + Contacts.TEST_HOST + "/WebDemo/UploadManager";
    final String FILE_PATH = Environment.getExternalStorageDirectory().toString() + File.separator + "test.zip";
    final String TAG = SimpleUploadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_download);

        ButterKnife.bind(this);

        mHandler = new DefaultHttpUploadHandler();
        ITask task = new TaskBuilder()
                .setName("test.zip")
                .setTaskId("1233444")
                .setSessionId("123123123131")
                .setDataSource(FILE_PATH)
                .setDestSource(URL)
                .build();
        mHandler.setTask(task);
        mHandler.setHandlerListenner(new SimpleTaskHandlerListenner() {
            @Override
            public void onPiceSuccessful(final ITask params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCompleteLength.setText(getFileSize(params.getCompleteLength()));
                        tvAllLength.setText(getFileSize(params.getLength()));

                        double progress = (double)params.getCompleteLength() / (double)params.getLength();
                        progress = progress * 100f;
                        progressLength.setProgress((int) progress);
                    }
                });
            }

            @Override
            public void onFinished(final ITask task) {
                super.onFinished(task);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCompleteLength.setText(getFileSize(task.getCompleteLength()));
                        tvAllLength.setText(getFileSize(task.getLength()));

                        double progress = (double)task.getCompleteLength() / (double)task.getLength();
                        progress = progress * 100f;
                        progressLength.setProgress((int) progress);
                    }
                });
                Debugger.error(TAG,"========onFinished============");
            }

            @Override
            public void onSpeedChanged(long speed, final ITask params) {
                super.onSpeedChanged(speed, params);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCompleteLength.setText(getFileSize(params.getCompleteLength()));
                        tvAllLength.setText(getFileSize(params.getLength()));

                        double progress = (double)params.getCompleteLength() / (double)params.getLength();
                        progress = progress * 100f;
                        progressLength.setProgress((int) progress);
                    }
                });
                Debugger.error("OnlyDownloadActivity","speed = " + getFileSize(speed) + "/s");
            }

            @Override
            public void onError(int code, ITask params) {
                super.onError(code, params);
                Debugger.error("SimpleUploadActivity","error " + code);
            }
        });

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,3,
                6000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10000));
        mHandler.setThreadPool(threadPool);
    }

    @OnClick(R.id.btn_stop)
    public void stop() {
        mHandler.stop();
    }


    @OnClick(R.id.btn_start)
    public void start() {
        mHandler.start();
    }

    private String getFileSize(long size) {
        if(size < 1024) {
            return size + "B";
        } else if(size < 1024 * 1024) {
            return size / 1024f + "KB";
        } else if(size < 1024 * 1024 * 1024) {
            return size / 1024f / 1024f + "MB";
        }
        return "";
    }
}
