package com.scott.example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scott.annotionprocessor.TaskType;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

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
        intent.putExtra(TaskFragment.EXTRA_TASK_TYPE,TaskType.TYPE_HTTP_UPLOAD);
        startActivity(intent);
    }

    @OnClick(R.id.btn_download_tasks)
    public void showDownloadTasks() {
        Intent intent = new Intent(this,SimpleTaskListActivity.class);
        intent.putExtra(TaskFragment.EXTRA_TASK_TYPE,TaskType.TYPE_HTTP_DOWNLOAD);
        startActivity(intent);
    }

    @OnClick(R.id.btn_create_task)
    public void createTask() {
        startActivity(new Intent(this,CreateTaskActivity.class));
    }
}
