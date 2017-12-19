package com.scott.example;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskType;
import com.scott.example.utils.Contacts;
import com.scott.transer.event.TaskEventBus;
import com.scott.transer.processor.ITaskCmd;
import com.scott.transer.processor.TaskCmdBuilder;
import com.scott.transer.task.TaskBuilder;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateTaskActivity extends AppCompatActivity {

    @BindView(R.id.edit_source)
    EditText editPath;

    @BindView(R.id.edit_dest)
    EditText editUrl;

    final TaskType TASK_TYPE = TaskType.TYPE_DOWNLOAD;

    final String NAME = "test.zip";
    final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + File.separator + "test.zip";
    final String DOWNLOAD_URL = "http://" + Contacts.TEST_HOST + "/WebDemo/test.zip";
    final String UPLOAD_PATH = DOWNLOAD_PATH;
    final String UPLOAD_URL = "http://" + Contacts.TEST_HOST + "/WebDemo/UploadManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);

        switch (TASK_TYPE) {
            case TYPE_DOWNLOAD:
                editPath.setText(DOWNLOAD_PATH);
                editUrl.setText(DOWNLOAD_URL);
                break;
            case TYPE_UPLOAD:
                editPath.setText(UPLOAD_PATH);
                editUrl.setText(UPLOAD_URL);
                break;
        }

    }

    @OnClick(R.id.btn_create_task)
    public void createTask() {

        String source = null;
        String dest = null;

        switch (TASK_TYPE) {
            case TYPE_UPLOAD:
                source = editPath.getText().toString();
                dest = editUrl.getText().toString();
                break;
            case TYPE_DOWNLOAD:
                dest = editPath.getText().toString();
                source = editUrl.getText().toString();
                break;
        }

        ITask task = new TaskBuilder()
                .setTaskType(TASK_TYPE)
                .setDataSource(source)
                .setDestSource(dest)
                .setTaskType(TASK_TYPE)
                .setName(NAME)
                .build();

        ITaskCmd cmd = new TaskCmdBuilder()
                .setTaskType(TASK_TYPE)
                .setProcessType(ProcessType.TYPE_ADD_TASK)
                .setTask(task)
                .build();

        TaskEventBus.getDefault().execute(cmd);
    }
}
