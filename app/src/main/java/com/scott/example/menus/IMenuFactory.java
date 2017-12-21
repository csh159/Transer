package com.scott.example.menus;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/3/27.</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface IMenuFactory {
    List<MenuItemInfo> getDefaultMenu();

    List<MenuItemInfo> getMenu(MenuItemInfo.IContentItem item);

    List<MenuItemInfo> getMenus(List<? extends MenuItemInfo.IContentItem> items);

    MenuItemInfo getMenu(int id);

    List<MenuItemInfo> getMenu(List<Integer> ids);

    void removeMenu(int id, List<MenuItemInfo> menus);

    List<MenuItemInfo> onInitMenus();
}
