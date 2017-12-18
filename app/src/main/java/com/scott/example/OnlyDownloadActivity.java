package com.scott.example;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.task.DefaultHttpDownloadHandler;
import com.scott.transer.task.ITaskHandler;
import com.scott.transer.task.SimpleTaskHandlerListenner;

import java.io.File;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnlyDownloadActivity extends AppCompatActivity {

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

    @BindView(R.id.btn_pause)
    Button btnPause;

    @BindView(R.id.btn_stop)
    Button btnStop;

    @BindView(R.id.btn_resume)
    Button btnResume;

    private ITaskHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_download);

        ButterKnife.bind(this);
        mHandler = new DefaultHttpDownloadHandler();
        ITask task = new TaskBuilder()
                .setDataSource("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
                .setDestSource(Environment.getExternalStorageDirectory().toString() + File.separator + "zzz.mp4")
                .build();
        mHandler.setTask(task);
        mHandler.setHandlerListenner(new SimpleTaskHandlerListenner() {
            @Override
            public void onPiceSuccessful(final ITask params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvCompleteLength.setText(params.getCompleteLength() + "");
                        tvAllLength.setText(params.getLength() + "");

                        double progress = params.getCompleteLength() / params.getLength();
                        progress = progress * 100;
                        progressLength.setProgress((int) progress);
                    }
                });
            }

            @Override
            public void onFinished(ITask task) {
                super.onFinished(task);
            }

        });
        mHandler.setThreadPool(Executors.newSingleThreadExecutor());
    }

    @OnClick(R.id.btn_stop)
    public void stop() {
        mHandler.stop();
    }

    @OnClick(R.id.btn_pause)
    public void pause() {
        mHandler.pause();
    }

    @OnClick(R.id.btn_start)
    public void start() {
        mHandler.start();
    }

    @OnClick(R.id.btn_resume)
    public void resume() {
        mHandler.resume();
    }
}
