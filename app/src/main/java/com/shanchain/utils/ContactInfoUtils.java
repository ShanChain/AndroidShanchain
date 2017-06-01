package com.shanchain.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.shanchain.mvp.model.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周建 on 2017/5/31.
 */

public class ContactInfoUtils {
    /**
     * 获取所有的联系人信息
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<ContactInfo> getAllContactInfos(Context context) {
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        ContentResolver resolver = context.getContentResolver();
        // 查询raw_contact表

        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
                null, null, null);
        if (cursor == null){
            LogUtils.d("没有联系人,或无法读取联系人");
            return null;
        }
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            System.out.println("Id:" + id);
            if (id != null) {
                ContactInfo info = new ContactInfo();
                // 查询data表
                Cursor datacursor = resolver.query(datauri, new String[] {
                                "data1", "mimetype" }, "raw_contact_id=?",
                        new String[] { id }, null);
                while (datacursor.moveToNext()) {
                    String data1 = datacursor.getString(0);
                    String mimetype = datacursor.getString(1);
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        info.setName(data1);
                    } else if ("vnd.android.cursor.item/im".equals(mimetype)) {
                        info.setQq(data1);
                    } else if ("vnd.android.cursor.item/email_v2"
                            .equals(mimetype)) {
                        info.setEmail(data1);
                    } else if ("vnd.android.cursor.item/phone_v2"
                            .equals(mimetype)) {
                        info.setPhone(data1);
                    }
                }
                datacursor.close();
                infos.add(info);
            }
        }
        cursor.close();
        return infos;
    }
}
