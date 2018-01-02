package com.scott.transer.utils;

import android.util.Log;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/18</P>
 * <P>Email: shilec@126.com</p>
 */

public class Debugger {

    static boolean DEBUG = false;

    public static void info(String tag,String info) {

        if(DEBUG) {
            Log.i(tag, info);
        }
    }

    public static void error(String tag,String info) {
        if(DEBUG) {
            Log.e(tag,info);
        }
    }
}
