package com.shanchain.arkspot.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhoujian on 2017/9/8.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "arkspot.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "t_contact";
    private static final String USERNAME = "username";
    private static final String CONTACT = "contact";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + " (_id integer primary key,"
                + USERNAME + " varchar(20)," + CONTACT + " varchar(20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
