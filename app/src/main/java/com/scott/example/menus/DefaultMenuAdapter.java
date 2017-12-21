package com.scott.example.menus;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.scott.example.R;

import java.util.List;

/**
 * Author:    shijiale
 * Date:      2016/12/2.
 * Email:     shilec@126.com
 * Describe: 默认操作菜单适配器
 */

public class DefaultMenuAdapter extends AbsViewAdapter<MenuItemInfo> {

    protected int mColnms;
    protected float mColmnWidth; //每个item的宽高
    protected final int SPACE_SIZE = 2; //条目间隔(dp)
    private GridView mGrid;

    private DefaultMenuAdapter(List<MenuItemInfo> datas, Context context) {
        super(context,datas);
    }

    /***
     *
     * @param datas menu数据
     * @param context
     * @param colmns 列数
     * @param gv gridview
     */
    public DefaultMenuAdapter(List<MenuItemInfo> datas, Context context,
                              int colmns, GridView gv) {
        this(datas, context);
        mColnms = colmns;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        mColmnWidth = (width - (colmns + 1) * getDpValue(SPACE_SIZE)) / colmns;
        gv.setNumColumns(mColnms);
        gv.setHorizontalSpacing((int) getDpValue(SPACE_SIZE));
        gv.setVerticalSpacing((int) getDpValue(SPACE_SIZE));
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) gv.getLayoutParams();
        lp.leftMargin = (int) getDpValue(SPACE_SIZE);
        mGrid = gv;
    }

    //dp value
    private float getDpValue(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, mContext.getResources().getDisplayMetrics());
    }

    private float getSpVlaue(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp,mContext.getResources().getDisplayMetrics());
    }

    @Override
    protected ViewHolder onCreateViewHolder(View itemView) {
        Holder h = new Holder(itemView);
        h.ivIcon = (TextView) itemView.findViewById(R.id.tv_icon);
        h.tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
        //LogUtil.i(TAG, "mWidth:" + mColmnWidth);
        //设置item为宽高都为mColmnWidth的正方形
        float tvHeight = getSpVlaue(14);
        itemView.setLayoutParams(new ViewGroup.LayoutParams((int) mColmnWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
        return h;
    }


    @Override
    protected void onBindData(ViewHolder vh, int position) {
        Holder h = (Holder) vh;
        h.tvTag.setText(mDatas.get(position).getTag());
        h.ivIcon.setBackgroundResource(mDatas.get(position).getResIcon());
    }

    @Override
    protected int onInflateItemView(int type) {
        return R.layout.item_opera_menu;
    }

    private final class Holder extends ViewHolder {

        protected Holder(View v) {
            super(v);
        }

        TextView ivIcon;
        TextView tvTag;
    }

    @Override
    public long getItemId(int i) {
        return mDatas.get(i).getId();
    }
}
