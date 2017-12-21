package com.scott.example.adapter;

import android.content.Context;
import android.view.View;

import com.scott.annotionprocessor.ITask;
import com.scott.example.menus.AbsViewAdapter;
import com.scott.transer.processor.ITaskCmd;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-21 16:31</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class DataSourceListAdapter extends AbsViewAdapter<ITask>{
    private List<ITask> mTasks;

    public DataSourceListAdapter(Context context, List<ITask> datas) {
        super(context, datas);
    }

    @Override
    protected ViewHolder onCreateViewHolder(View itemView) {
        return null;
    }

    @Override
    protected void onBindData(ViewHolder holder, int posistion) {

    }

    @Override
    protected int onInflateItemView(int type) {
        return 0;
    }
}
