package com.shanchain.shandata.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.shanchain.shandata.mvp.model.ContactInfo;

import java.util.ArrayList;

public class ContactsUtils {
    public static ArrayList<ContactInfo> getAllCallRecords(Context context) {

        ArrayList<ContactInfo> list = new ArrayList<>();

        Cursor c = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC");

        while (c.moveToNext()) {
            ContactInfo info = new ContactInfo();

            // 获得联系人的ID号
            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

            // 获得联系人姓名
            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            info.setName(name);

            // 查看该联系人有多少个电话号码。如果没有这返回值为0
            int phoneCount = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            String number = null;
            if (phoneCount > 0) {
                // 获得联系人的电话号码
                Cursor phones = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + contactId, null, null);
                if (phones.moveToFirst()) {
                    number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    info.setPhone(number);

                }
                phones.close();
            }

            list.add(info);

        }
        c.close();
        return list;
    }
}
