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
import com.scott.example.utils.TaskUtils;
import com.scott.transer.task.BaseTaskHandler;
import com.scott.transer.task.HandlerParamNames;
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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

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

    @BindView(R.id.tv_speed)
    TextView tvSpeed;

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
        //创建一个任务
        ITask task = new TaskBuilder()
                .setName("test.zip") //设置任务名称
                .setDataSource(URL)  //设置数据源
                .setDestSource(FILE_PATH) //设置目标路径
                .build();
        mHandler.setTask(task);

        //设置请求参数
        Map<String,String> params = new HashMap<>();
        params.put("path","test.zip");
        params.put(HandlerParamNames.PARAM_SPEED_LIMITED, BaseTaskHandler.SPEED_LISMT.SPEED_UNLIMITED + "");
        mHandler.setParams(params);
        mHandler.setHandlerListenner(new DownloadListener());

        //设置一个线程池去下载文件，如果不设置，则会在当前线程进行下载。
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

    private final class DownloadListener extends SimpleTaskHandlerListenner {

        @Override
        public void onPiceSuccessful(final ITask params) {

            //Debugger.error(TAG,"finished === " + params);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCompleteLength.setText(TaskUtils.getFileSize(params.getCompleteLength()));
                    tvAllLength.setText(TaskUtils.getFileSize(params.getLength()));

                    double progress = (double)params.getCompleteLength() / (double)params.getLength();
                    progress = progress * 100f;
                    progressLength.setProgress((int) progress);
                    tvName.setText(params.getName());
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
            final String newMd5 = TaskUtils.getFileMD5(new File(FILE_PATH));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCompleteLength.setText(TaskUtils.getFileSize(task.getCompleteLength()));
                    tvAllLength.setText(TaskUtils.getFileSize(task.getLength()));

                    double progress = (double)task.getCompleteLength() / (double)task.getLength();
                    progress = progress * 100f;
                    progressLength.setProgress((int) progress);
                    tvNewMd5.setText(newMd5);
                    tvEquals.setText(TextUtils.equals(newMd5,FILE_MD5) + "");
                }
            });
        }

        @Override
        public void onSpeedChanged(long speed, final ITask params) {
            super.onSpeedChanged(speed, params);
            Debugger.error("OnlyDownloadActivity","speed = " + TaskUtils.getFileSize(speed) + "/s");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvSpeed.setText(TaskUtils.getFileSize(params.getSpeed()));
                }
            });
        }
    }
}
