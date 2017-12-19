package com.scott.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scott.annotionprocessor.ITask;
import com.scott.example.R;
import com.scott.example.utils.SizeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/19</P>
 * <P>Email: shilec@126.com</p>
 */

public class TaskListAdapter extends BaseAdapter implements View.OnClickListener{

    private List<ITask> mTasks;

    public TaskListAdapter(List<ITask> tasks) {
        mTasks = tasks;
    }

    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public Object getItem(int i) {
        return mTasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TaskViewHolder holder = null;
        if(view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task_item, viewGroup, false);
            holder = new TaskViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (TaskViewHolder) view.getTag();
        }

        holder.btnPasue.setOnClickListener(this);
        holder.btnStart.setOnClickListener(this);
        holder.btnResume.setOnClickListener(this);
        holder.btnStop.setOnClickListener(this);

        ITask task = mTasks.get(i);
        holder.tvName.setText(task.getName());
        holder.tvCompleteLength.setText(SizeUtils.getFileSize(task.getCompleteLength()));
        holder.tvLength.setText(SizeUtils.getFileSize(task.getLength()));
        holder.tvSpeed.setText(SizeUtils.getFileSize(task.getSpeed()));

        double progress = (double)task.getCompleteLength() / (double)task.getLength();
        progress = progress * 100f;
        holder.progressLength.setProgress((int) progress);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    public static final class TaskViewHolder {

        View itemView;

        TaskViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }

        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.tv_complete_length)
        TextView tvCompleteLength;

        @BindView(R.id.tv_speed)
        TextView tvSpeed;

        @BindView(R.id.tv_all_length)
        TextView tvLength;

        @BindView(R.id.btn_start)
        Button btnStart;

        @BindView(R.id.btn_stop)
        Button btnStop;

        @BindView(R.id.btn_pause)
        Button btnPasue;

        @BindView(R.id.btn_resume)
        Button btnResume;

        @BindView(R.id.progress_length)
        ProgressBar progressLength;
    }
}
