package com.scott.example;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.ProcessType;
import com.scott.annotionprocessor.TaskSubscriber;
import com.scott.annotionprocessor.TaskType;
import com.scott.annotionprocessor.ThreadMode;
import com.scott.example.adapter.TaskListAdapter;
import com.scott.transer.event.TaskEventBus;
import com.scott.transer.processor.ITaskCmd;
import com.scott.transer.processor.TaskCmdBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/19</P>
 * <P>Email: shilec@126.com</p>
 */

public class TaskFragment extends Fragment {

    private List<ITask> mTasks = new ArrayList<>();
    private TaskListAdapter mTaskAdapter;
    private ListView mListView;
    private final String TAG = TaskFragment.class.getSimpleName();
    private TaskType mTaskType;
    public static final String EXTRA_TASK_TYPE = "task_type";

    @Override
    public void onStart() {
        super.onStart();
        mTaskType = (TaskType) getArguments().get(EXTRA_TASK_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
        mListView = root.findViewById(R.id.rcy_tasks);
        mTaskAdapter = new TaskListAdapter(mTasks);
        mListView.setAdapter(mTaskAdapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        TaskEventBus.getDefault().regesit(this);

        ITaskCmd cmd = new TaskCmdBuilder()
                .setTaskType(mTaskType)
                .setProcessType(ProcessType.TYPE_QUERY_TASKS_ALL)
                .build();
        TaskEventBus.getDefault().execute(cmd);
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskEventBus.getDefault().unregesit(this);
    }

    @TaskSubscriber(taskType = TaskType.TYPE_HTTP_DOWNLOAD,threadMode = ThreadMode.MODE_MAIN)
    public void onDownloadTasksChange(final List<ITask> tasks) {

        if(mTaskType != TaskType.TYPE_HTTP_DOWNLOAD) return;
        //Debugger.error(TAG,tasks.toString());
        onTasksChange(tasks);
    }

    @TaskSubscriber(taskType = TaskType.TYPE_HTTP_UPLOAD,threadMode = ThreadMode.MODE_MAIN)
    public void onUploadTaskChange(final List<ITask> tasks) {
        if(mTaskType != TaskType.TYPE_HTTP_UPLOAD) return;
        onTasksChange(tasks);
    }

    private void onTasksChange(final List<ITask> tasks) {

        //为了保持任务管理的一致。不能将ITask 转为 Task
        //只是用ITask 去获取任务信息，显示到UI
        if (tasks == null) {
            return;
        }
        Log.e(TAG,"thread ==== " + Thread.currentThread().getName());
        mTasks.clear();
        mTasks.addAll(tasks);
        mTaskAdapter.notifyDataSetChanged();
    }
}
