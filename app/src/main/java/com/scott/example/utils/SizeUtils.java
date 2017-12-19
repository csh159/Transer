package com.scott.example.utils;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/19</P>
 * <P>Email: shilec@126.com</p>
 */

public class SizeUtils {

    public static String getFileSize(long size) {
        if(size < 1024) {
            return size + "B";
        } else if(size < 1024 * 1024) {
            return size / 1024f + "KB";
        } else if(size < 1024 * 1024 * 1024) {
            return size / 1024f / 1024f + "MB";
        }
        return "";
    }
}
