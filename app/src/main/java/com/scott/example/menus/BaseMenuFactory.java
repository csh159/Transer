package com.scott.example.menus;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/4/18.</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public abstract class BaseMenuFactory implements IMenuFactory{

    private List<MenuItemInfo> mMenus;
    protected Context mContext;

    public BaseMenuFactory(Context context) {
        mContext = context;
        mMenus = onInitMenus();
    }

    @Override
    public MenuItemInfo getMenu(int id) {
        for (MenuItemInfo menu : mMenus) {
            if (id == menu.getId()) {
                return menu;
            }
        }
        return null;
    }

    @Override
    public List<MenuItemInfo> getMenu(List<Integer> ids) {
        List<MenuItemInfo> menus = new ArrayList<>();
        for (int id : ids) {
            menus.add(getMenu(id));
        }
        return menus;
    }


    @Override
    public List<MenuItemInfo> getMenus(List<? extends MenuItemInfo.IContentItem> items) {
        List<MenuItemInfo> menus;
        if (items == null || items.isEmpty()) return null;
        menus = getMenu(items.get(0));
        for (int i = 1; i < items.size(); i++) {
            MenuItemInfo.IContentItem item = items.get(i);

            List<MenuItemInfo> tempMenus;
            List<MenuItemInfo> tempMenus1;
            if (menus.size() < getMenu(item).size()) {
                tempMenus = getMenu(item);
                tempMenus1 = new ArrayList<>();
                tempMenus1.addAll(menus);
            } else {
                tempMenus1 = getMenu(item);
                tempMenus = new ArrayList<>();
                tempMenus.addAll(menus);
            }
            menus.clear();
            //取交集
            for (MenuItemInfo menu : tempMenus1) {
                if (isHaveMenu(menu, tempMenus)) {
                    menus.add(menu);
                }
            }
        }
        return menus;
    }

    public boolean isHaveMenu(MenuItemInfo menu, List<MenuItemInfo> menus) {
        for (MenuItemInfo m : menus) {
            if (m.getId() == menu.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeMenu(int id, List<MenuItemInfo> menus) {
        for (MenuItemInfo menu : menus) {
            if (id == menu.getId()) {
                menus.remove(menu);
                return;
            }
        }
    }
}
