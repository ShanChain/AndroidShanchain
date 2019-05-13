package com.shanchain.shandata.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.shanchain.data.common.utils.LogUtils;

import org.greenrobot.greendao.database.Database;

/**
 * Created by WealChen
 * Date : 2019/5/13
 * Describe :
 */
public class MyDbOpenHelper extends DaoMaster.OpenHelper {

    public MyDbOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogUtils.d("DataBaseVersion", "之前的版本："+oldVersion + "---更新之后的版本---" + newVersion);
        MigrationHelper.getInstance().migrate(db, ConversationEntryDao.class, MessageEntryDao.class);
//        DaoMaster.dropAllTables(db,true);
//        DaoMaster.createAllTables(db,false);
    }
}
