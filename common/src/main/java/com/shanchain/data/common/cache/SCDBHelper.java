package com.shanchain.data.common.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;

/**
 * Created by flyye on 2017/10/10.
 */

public class SCDBHelper extends SQLiteOpenHelper {

    private String[] sqlArr;

    public SCDBHelper(Context context, String name, int version, boolean isInnerDb) {
        super(context, name, null, version);
        if (Build.VERSION.SDK_INT >= 11) {
            getReadableDatabase().enableWriteAheadLogging();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.sqlArr = CreateTableConstant.INIT_TABLE;
        if (this.sqlArr != null && this.sqlArr.length > 0) {
            for (String sql : sqlArr) {
                if (!TextUtils.isEmpty(sql)) db.execSQL(sql);
            }
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
