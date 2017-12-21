package com.scott.example;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskSubscriber;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.event.TaskEventBus;
import com.scott.transer.task.TaskBuilder;
import com.scott.transer.task.DefaultHttpDownloadHandler;
import com.scott.transer.task.ITaskHandler;
import com.scott.transer.task.SimpleTaskHandlerListenner;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        AndPermission.with(this)
                .permission(Permission.STORAGE)
                .callback(this)
                .start();
    }

    @OnClick(R.id.btn_simple_download)
    public void onSimpleDownload() {
        startActivity(new Intent(this,SimpleDownloadActivity.class));
    }

    @OnClick(R.id.btn_simple_upload)
    public void onSimpleUpload() {
        startActivity(new Intent(this,SimpleUploadActivity.class));
    }

    @OnClick(R.id.btn_upload_tasks)
    public void showUploadTasks() {
        Intent intent = new Intent(this,SimpleTaskListActivity.class);
        intent.putExtra(TaskFragment.EXTRA_TASK_TYPE,TaskType.TYPE_UPLOAD);
        startActivity(intent);
    }

    @OnClick(R.id.btn_download_tasks)
    public void showDownloadTasks() {
        Intent intent = new Intent(this,SimpleTaskListActivity.class);
        intent.putExtra(TaskFragment.EXTRA_TASK_TYPE,TaskType.TYPE_DOWNLOAD);
        startActivity(intent);
    }

    @OnClick(R.id.btn_create_task)
    public void createTask() {
        startActivity(new Intent(this,CreateTaskActivity.class));
    }
}
