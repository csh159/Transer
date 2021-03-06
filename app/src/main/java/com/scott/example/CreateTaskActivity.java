package com.scott.example;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

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
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class CreateTaskActivity extends AppCompatActivity {

    @BindView(R.id.edit_source)
    EditText editPath;

    @BindView(R.id.edit_dest)
    EditText editUrl;

    @BindView(R.id.rg_type)
    RadioGroup radioGroup;

    private TaskType task_type = TaskType.TYPE_HTTP_UPLOAD;

    final String NAME = "test.zip";
    final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().toString() + File.separator + NAME;
    final String DOWNLOAD_URL = "http://" + Contacts.TEST_HOST + "/WebDemo/DownloadManager";
    ;
    final String UPLOAD_PATH = DOWNLOAD_PATH;
    final String UPLOAD_URL = "http://" + Contacts.TEST_HOST + "/WebDemo/UploadManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);

        switch (task_type) {
            case TYPE_HTTP_DOWNLOAD:
                editPath.setText(DOWNLOAD_PATH);
                editUrl.setText(DOWNLOAD_URL);
                break;
            case TYPE_HTTP_UPLOAD:
                editPath.setText(UPLOAD_PATH);
                editUrl.setText(UPLOAD_URL);
                break;
        }
    }

    @OnCheckedChanged(R.id.rb_upload)
    public void uploadCheck(boolean checked) {
        if(checked) {
            editPath.setText(UPLOAD_PATH);
            editUrl.setText(UPLOAD_URL);
        }
    }

    @OnCheckedChanged(R.id.rb_download)
    public void downloadCheck(boolean checked) {
        if(checked) {
            editUrl.setText(DOWNLOAD_URL);
            editPath.setText(DOWNLOAD_PATH);
        }
    }

    @OnClick(R.id.btn_create_task)
    public void createTask() {

        String source = null;
        String dest = null;

        if(radioGroup.getCheckedRadioButtonId() == R.id.rb_upload) {
            task_type = TaskType.TYPE_HTTP_UPLOAD;
        } else {
            task_type = TaskType.TYPE_HTTP_DOWNLOAD;
        }

        switch (task_type) {
            case TYPE_HTTP_UPLOAD:
                source = editPath.getText().toString();
                dest = editUrl.getText().toString();
                break;
            case TYPE_HTTP_DOWNLOAD:
                dest = editPath.getText().toString();
                source = editUrl.getText().toString();
                break;
        }

        ITask task = new TaskBuilder()
                .setTaskType(task_type)
                .setDataSource(source)
                .setDestSource(dest)
                .setName(NAME)
                .build();

        ITaskCmd cmd = new TaskCmdBuilder()
                .setTaskType(task_type)
                .setProcessType(ProcessType.TYPE_ADD_TASK)
                .setTask(task)
                .build();

        TaskEventBus.getDefault().execute(cmd);
    }
}
