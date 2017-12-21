package com.scott.example.menus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author:    shijiale
 * Date:      2016/12/2.
 * Email:     shilec@126.com
 * Describe:
 */

public class MenuItemInfo {

    private String tag;
    private int resIcon;
    private int id;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public MenuItemInfo(String tag, int resIcon, int id) {
        this.tag = tag;
        this.id = id;
        this.resIcon = resIcon;
    }


    public MenuItemInfo() {

    }

    public static interface IMenuItemMethod {
    }

    public static interface IContentItem {

    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface MenuMethod {
        int value() default -1;
    }
}
