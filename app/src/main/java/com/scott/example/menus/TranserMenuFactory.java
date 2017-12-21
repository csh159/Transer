package com.scott.example.menus;

import android.content.Context;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-21 16:37</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TranserMenuFactory extends BaseMenuFactory {
    public TranserMenuFactory(Context context) {
        super(context);
    }

    @Override
    public List<MenuItemInfo> getDefaultMenu() {
        return null;
    }

    @Override
    public List<MenuItemInfo> getMenu(MenuItemInfo.IContentItem item) {
        return null;
    }

    @Override
    public List<MenuItemInfo> onInitMenus() {
        return null;
    }
}
