package com.scott.example.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/7/7.</p>
 * <p>Describe:</p>
 */

public  class BaseListHolder {

    private int itemType;
    protected View itemView;
    private SparseArray<View> views;

    public BaseListHolder(View itemView) {
        this.itemView = itemView;
        views = new SparseArray<>();
    }

    public void setItemType(int type) {
        itemType = type;
    }

    public int getItemViewType() {
        return itemType;
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public void setImage(int resId,int imgid) {
        ImageView iv = getView(resId);
        iv.setImageResource(imgid);
    }

    public void setText(int resId,String text) {
        TextView tv = getView(resId);
        tv.setText(text);
    }

    public void setProgress(int resId,int progress) {
        ProgressBar probar = getView(resId);
        probar.setProgress(progress);
    }

    public void setOnClickListenner(int resId, View.OnClickListener l) {
        View v = getView(resId);
        v.setOnClickListener(l);
    }

}
