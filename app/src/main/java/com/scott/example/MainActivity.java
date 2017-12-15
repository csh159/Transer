package com.scott.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskSubscriber;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.event.TaskEventBus;
import com.scott.transer.task.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @TaskSubscriber(processType = ProcessType.TYPE_CHANGE_TASK)
    public void onTaskStop() {
        TaskEventBus.getDefault().post(null);
    }

    @TaskSubscriber(processType = {ProcessType.TYPE_CHANGE_TASK,ProcessType.TYPE_ADD_TASKS})
    public void stop() {

    }

    @TaskSubscriber(processType =
            {ProcessType.TYPE_CHANGE_TASK,ProcessType.TYPE_ADD_TASKS}
            ,taskType = TaskType.TYPE_DOWNLOAD)
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
}
