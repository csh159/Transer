package com.scott.example;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scott.annotionprocessor.ITask;
import com.scott.example.utils.Contacts;
import com.scott.transer.task.TaskBuilder;
import com.scott.transer.task.DefaultHttpDownloadHandler;
import com.scott.transer.task.ITaskHandler;
import com.scott.transer.task.SimpleTaskHandlerListenner;
import com.scott.transer.utils.Debugger;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SimpleDownloadActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_complete_length)
    TextView tvCompleteLength;

    @BindView(R.id.tv_all_length)
    TextView tvAllLength;

    @BindView(R.id.progress_length)
    ProgressBar progressLength;

    @BindView(R.id.btn_start)
    Button btnStart;

    @BindView(R.id.btn_stop)
    Button btnStop;

    @BindView(R.id.tv_md5)
    TextView tvMd5;

    @BindView(R.id.tv_md5_new)
    TextView tvNewMd5;

    @BindView(R.id.tv_equals)
    TextView tvEquals;

    private ITaskHandler mHandler;

    final String URL = "http://" + Contacts.TEST_HOST + "/WebDemo/DownloadManager";
    final String FILE_PATH = Environment.getExternalStorageDirectory().toString() + File.separator + "test.zip";
    final String FILE_MD5 = "de37fe1c8f049bdd83090d40f806cd67";
    final String TAG = SimpleDownloadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_download);

        ButterKnife.bind(this);
        tvMd5.setText(FILE_MD5);

        mHandler = new DefaultHttpDownloadHandler();
        ITask task = new TaskBuilder()
                .setDataSource(URL)
                .setDestSource(FILE_PATH)
                .build();

        mHandler.setTask(task);
        Map<String,String> params = new HashMap<>();
        params.put("path","test.zip");
        mHandler.setParams(params);
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
            public void onError(int code, ITask params) {
                super.onError(code, params);
                Debugger.error(TAG,"error === " + params);
            }

            @Override
            public void onFinished(final ITask task) {
                Debugger.error(TAG,"finished === " + task);
                super.onFinished(task);
                final String newMd5 = getFileMD5(new File(FILE_PATH));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCompleteLength.setText(getFileSize(task.getCompleteLength()));
                        tvAllLength.setText(getFileSize(task.getLength()));

                        double progress = (double)task.getCompleteLength() / (double)task.getLength();
                        progress = progress * 100f;
                        progressLength.setProgress((int) progress);
                        tvNewMd5.setText(newMd5);
                        tvEquals.setText(TextUtils.equals(newMd5,FILE_MD5) + "");
                    }
                });
            }

            @Override
            public void onSpeedChanged(long speed, ITask params) {
                super.onSpeedChanged(speed, params);
                //Debugger.error("OnlyDownloadActivity","speed = " + getFileSize(speed) + "/s");
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

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
