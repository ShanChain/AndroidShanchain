package com.shanchain.arkspot.db;

import android.content.Context;

public class ContactDao {
    private static Context mContext;
    public static void initContactDao(Context context){
        mContext = context.getApplicationContext();
    }


}
