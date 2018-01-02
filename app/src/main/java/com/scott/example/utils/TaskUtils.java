package com.scott.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/19</P>
 * <P>Email: shilec@126.com</p>
 */

public class TaskUtils {

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

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[4 * 1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
