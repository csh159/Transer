package com.scott.example;

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
import com.scott.transer.task.DefaultHttpDownloadHandler;
import com.scott.transer.task.ITaskHandler;
import com.scott.transer.task.SimpleTaskHandlerListenner;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @TaskSubscriber(processType = ProcessType.TYPE_CHANGE_TASK)
    public void onTaskStop() {
        TaskEventBus.getDefault().execute(null);
    }

    @TaskSubscriber(processType = {ProcessType.TYPE_CHANGE_TASK})
    public void stop(List<ITask> taskList) {

    }

    @TaskSubscriber(processType =
            {ProcessType.TYPE_CHANGE_TASK, ProcessType.TYPE_ADD_TASK}
            , taskType = TaskType.TYPE_UPLOAD)
    public void stop2() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        TaskEventBus.getDefault().regesit(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TaskEventBus.getDefault().unregesit(this);
    }

    public void send(View v) {

//        List<ITask> tasks = new ArrayList<>();
//        for(int i = 0; i < 10; i++) {
//            ITask task = new TaskBuilder()
//                    .setDataSource("127.0.0.1:8080/upload")
//                    .setTaskType(TaskType.TYPE_UPLOAD)
//                    .build();
//            tasks.add(task);
//        }
//        TaskEventBus.getDefault().execute(new TaskCmdBuilder()
//                .setProcessType(ProcessType.TYPE_ADD_TASKS)
//                .setTasks(tasks).build());
        ITaskHandler handler = new DefaultHttpDownloadHandler();
        ITask task = new TaskBuilder()
                .setDataSource("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
                .setDestSource(Environment.getExternalStorageDirectory().toString() + File.separator + "zzz_123213.mp4")
                .build();
        handler.setTask(task);
        handler.setHandlerListenner(new SimpleTaskHandlerListenner() {
            @Override
            public void onPiceSuccessful(ITask params) {
                Log.e("MainActivity", params.toString());
            }

            @Override
            public void onFinished(ITask task) {
                Log.e("MainActivity","finished ==========");
            }
        });
        handler.setThreadPool(Executors.newSingleThreadExecutor());
        handler.start();
    }
}
