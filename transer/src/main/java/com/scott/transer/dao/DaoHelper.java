package com.scott.transer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * <P>Author: shijiale</P>
 * <P>Date: 2017/12/17</P>
 * <P>Email: shilec@126.com</p>
 */

public class DaoHelper {
    public static final String DB_NAME = "trans-db";
    private static DaoMaster.DevOpenHelper mDbHelper;
    private static SQLiteDatabase mDb;
    private static DaoMaster mDao;
    private static DaoSession mDbSession;


    public static void init(Context context) {
        mDbHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
        mDb = mDbHelper.getWritableDatabase();
        mDao = new DaoMaster(mDb);
        mDbSession = mDao.newSession();
    }

    public static DaoSession getDbSession() {
        return mDbSession;
    }

    public static SQLiteDatabase getDb() {
        return mDb;
    }

}
