package com.scott.example.menus;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.scott.example.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:    shijiale
 * Date:      2016/12/2.
 * Email:     shilec@126.com
 * Describe: 操作菜单弹出框
 **/

public class OperationMenuWindow implements AdapterView.OnItemClickListener {

    private PopupWindow mWindow;
    private GridView mGridView;
    private Context mContext;
    private BaseAdapter mAdapter;
    private boolean isVisiable = false;
    private Builder mBuilder;
    private OnFileItemChangeListenner mFileChangeListenner;

    public static final int MAX_CLOMNS = 5;

    //request code only 16bits

    public static class Builder {

        private List<? extends MenuItemInfo.IContentItem> mNowOperaItems;//当前需要操作的item
        private OperationMenuWindow mOpWindow;
        private IMenuFactory mFactory; //生成菜单的factory
        private MenuItemInfo.IMenuItemMethod mMethod;

        public Builder(Context context) {
            mOpWindow = new OperationMenuWindow(context);
        }

        public Builder setOperMethod(MenuItemInfo.IMenuItemMethod method) {
            mMethod = method;
            return this;
        }
        /***
         * 设置操作栏消失监听
         * @param l
         * @return
         */
        public Builder setOnDismissListenner(final PopupWindow.OnDismissListener l) {
            mOpWindow.mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (l != null) {
                        l.onDismiss();
                    }
                }
            });
            return this;
        }


        /***
         * 设置当操作的items发生改变时callback
         * @param l
         * @return
         */
        public Builder setOnFileItemChangeListenner(OnFileItemChangeListenner l) {
            mOpWindow.mFileChangeListenner = l;
            return this;
        }

        /***
         * 设置当前操作的items
         * @param items
         * @return
         */
        public Builder setOperItem(List<? extends MenuItemInfo.IContentItem> items) {
            mNowOperaItems = items;
            return this;
        }

        /***
         * 设置默认的显示风格
         * @param menus 菜单条目
         * @param maxClomns 列数
         * @return
         */
        private Builder setDefaultAdapter(List<MenuItemInfo> menus, int maxClomns) {
            mOpWindow.mAdapter = new DefaultMenuAdapter(menus, mOpWindow.mContext,
                    maxClomns, mOpWindow.mGridView);
            mOpWindow.mGridView.setAdapter(mOpWindow.mAdapter);
            return this;
        }

        /**
         * 设置默认显示风格
         * @param maxClomns 列数
         * @return
         */
        public Builder setDefaultAdapter(int maxClomns) {
            List<MenuItemInfo> menus = new ArrayList<>();
            setDefaultAdapter(menus, maxClomns);
            return this;
        }

        /***
         * 添加默认的菜单,有mFactory生成
         * @return
         */
        public Builder addDefaultMenu() {
            List<MenuItemInfo> menus = getNowMenuList();
            if (menus == null) {
                menus = new ArrayList<>();
            }
            menus.clear();
            if(mFactory != null && mFactory.getDefaultMenu() != null ) {
                menus.addAll(mFactory.getDefaultMenu());
            }
            return this;
        }

        /***
         * 必须设置menufactory,用于生成菜单
         * @param factory
         * @return
         */
        public Builder setMenuFactory(IMenuFactory factory) {
            mFactory = factory;
            return this;
        }

        /**
         * 当前显示的菜单列表
         * @return
         */
        private List<MenuItemInfo> getNowMenuList() {
            if(mOpWindow == null) {
                return null;
            }
            if(mOpWindow != null && mOpWindow.mAdapter == null) {
                setDefaultAdapter();
            }
            return ((AbsViewAdapter<MenuItemInfo>)
                    mOpWindow.mAdapter).getDatas();
        }

        /***
         * 添加一个菜单
         * @param menu
         * @return
         */
        public Builder addMenu(MenuItemInfo menu) {
            List<MenuItemInfo> menus = getNowMenuList();
            if (menus != null) {
                menus.add(menu);
            }
            return this;
        }

        /***
         * 添加一组菜单
         * @param menus
         * @return
         */
        public Builder addMenus(List<MenuItemInfo> menus) {
            for (MenuItemInfo menu : menus) {
                addMenu(menu);
            }
            return this;
        }

        /***
         * 删除一个菜单
         * @param id
         */
        public void removeMenu(int id) {
            List<MenuItemInfo> datas = getNowMenuList();
            for (MenuItemInfo info : datas) {
                if (info.getId() == id) {
                    datas.remove(info);
                    mOpWindow.mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }

        /***
         * 获取默认的菜单,有menufactory生成
         * @return
         */
        private List<MenuItemInfo> getDefaultMenu() {
            List<MenuItemInfo> defaultMenu;
            if(mFactory != null && mFactory.getDefaultMenu() != null) {
                defaultMenu = mFactory.getDefaultMenu();
            } else {
                defaultMenu = new ArrayList<>();
            }
            return defaultMenu;
        }

        private void setDefaultAdapter() {
            if(mOpWindow.mAdapter == null) {
                mOpWindow.mAdapter = new DefaultMenuAdapter(getDefaultMenu(), mOpWindow.mContext,
                        MAX_CLOMNS, mOpWindow.mGridView);
            }
        }

        public OperationMenuWindow build() {
            mOpWindow.mAdapter.notifyDataSetChanged();
            mOpWindow.mBuilder = this;
            return mOpWindow;
        }

    }

    public static interface OnFileItemChangeListenner {
        List<MenuItemInfo> onFileItemChanged(List<? extends MenuItemInfo.IContentItem> items, List<MenuItemInfo> defaultMenus);
    }

    public void removeMenu(int id) {
        if (mBuilder != null) {
            mBuilder.removeMenu(id);
        }
    }

//    /***
//     * 动态添加菜单
//     * @param menu
//     */
//    public void addMenu(MenuItemInfo<? extends MenuItemInfo.IMenuItemMethod> menu) {
//        if (mBuilder != null) {
//            mBuilder.addMenu(menu);
//        }
//        mAdapter.notifyDataSetChanged();
//    }

    public IMenuFactory getMenuFactory() {
        return mBuilder.mFactory;
    }

    /***
     * 当操作的item为多个时,当items改变,调用该方法,用来刷新当前显示的菜单
     * @param items
     */
    public void changeFileItems(List<? extends MenuItemInfo.IContentItem> items) {
        List<MenuItemInfo> menus;
        if(items != null) {
            mBuilder.mNowOperaItems = items;
        } else {
            return;
        }
        if (mFileChangeListenner != null) {
            menus = new ArrayList<>();
            menus.addAll(mBuilder.getDefaultMenu());
            if(mFileChangeListenner.onFileItemChanged(items,menus) != null) {
                menus = mFileChangeListenner.onFileItemChanged(items, menus);
            }
        } else {
            menus = onFileItemChanged(items, mBuilder.getDefaultMenu());
        }

        mBuilder.getNowMenuList().clear();
        mBuilder.getNowMenuList().addAll(menus);
        mAdapter.notifyDataSetChanged();
    }

    /***
     * 默认的items变更菜单生成规则
     * @param items
     * @param menus
     * @return
     */
    private List<MenuItemInfo>
    onFileItemChanged(List<? extends MenuItemInfo.IContentItem> items,
                      List<MenuItemInfo> menus) {
        if(mBuilder.mFactory != null) {
            return mBuilder.mFactory.getMenus(items);
        } else {
            return mBuilder.getDefaultMenu();
        }
    }

    public boolean getVisiable() {
        return isVisiable;
    }

    private OperationMenuWindow(Context context) {
        mContext = context;
        initWindow();
    }

    /**
     * 设置window的属性
     */
    private void initWindow() {

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        View v = LayoutInflater.from(mContext).inflate(R.layout.window_operation, null, false);
        v.setLayoutParams(lp);

        mWindow = new PopupWindow(mContext);
        mWindow.setContentView(v);
        mWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
        mWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //mWindow.setAnimationStyle(R.style.popwin_anim_style);
        mWindow.setOutsideTouchable(false);

        initViews(v);
    }

    private void initViews(View contentView) {
        mGridView = (GridView) contentView.findViewById(R.id.grid_menu);
        //ViewGroup.LayoutParams lp = mGridView.getLayoutParams();
        //lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        //mGridView.setLayoutParams(lp);
        if (mAdapter != null)
            mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mWindow.dismiss();
        if(mBuilder.mMethod == null) return;
        MenuItemInfo info = mBuilder.getNowMenuList().get(position);
        Class cls = mBuilder.mMethod.getClass();
        Method[] methods = cls.getDeclaredMethods();

        for (Method m : methods) {
            MenuItemInfo.MenuMethod method = m.getAnnotation(MenuItemInfo.MenuMethod.class);
            if (method == null) continue;
            int injectId = method.value(); //根据注解调用指定的方法
            if(injectId == -1) continue;
            if (injectId == info.getId()) {
                try {
                    m.setAccessible(true);
                    m.invoke(mBuilder.mMethod, mBuilder.mNowOperaItems);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void show(View v) {
        mWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    public void dismiss() {
        mWindow.dismiss();
    }
}
