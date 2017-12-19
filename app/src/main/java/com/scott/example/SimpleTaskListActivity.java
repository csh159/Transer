package com.scott.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scott.annotionprocessor.TaskType;

public class SimpleTaskListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_task_list);

        TaskType taskType = (TaskType) getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_TYPE);

        TaskFragment taskFragment = new TaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskFragment.EXTRA_TASK_TYPE, taskType);
        taskFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.rl_container,taskFragment)
                .commit();
    }
}
