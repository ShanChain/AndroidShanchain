package com.shanchain.shandata.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactDao {
    private static Context mContext;

    public static void initContactDao(Context context) {
        mContext = context.getApplicationContext();
    }


    /**
     * 描述：获取本地联系人
     */
    public static List<String> getContacts(String username) {
        if (mContext == null) {
            throw new RuntimeException("使用ContactDao之前请 现在Application中初始化！");
        }
        DataBaseHelper openHelper = new DataBaseHelper(mContext);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CONTACT, new String[]{DataBaseHelper.CONTACT},
                DataBaseHelper.USERNAME + "=?", new String[]{username}, null, null, DataBaseHelper.CONTACT);
        List<String> contactsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String contact = cursor.getString(0);
            contactsList.add(contact);
        }
        cursor.close();
        database.close();
        return contactsList;
    }

    /**
     * 更新本地联系人
     *
     * @param username
     * @param contactsList 1. 先删除username的所有的联系人
     *                     2. 再添加contactsList添加进去
     */
    public static void updateContacts(String username, List<String> contactsList) {
        DataBaseHelper openHelper = new DataBaseHelper(mContext);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        database.beginTransaction();
        database.delete(DataBaseHelper.TABLE_CONTACT, DataBaseHelper.USERNAME + "=?", new String[]{username});
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.USERNAME, username);
        for (int i = 0; i < contactsList.size(); i++) {
            String contact = contactsList.get(i);
            values.put(DataBaseHelper.CONTACT, contact);
            database.insert(DataBaseHelper.TABLE_CONTACT, null, values);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

}
