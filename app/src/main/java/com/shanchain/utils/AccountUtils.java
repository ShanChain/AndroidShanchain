package com.shanchain.utils;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * Created by zhoujian on 2017/5/19.
 * 账号工具类
 */

public class AccountUtils {

    private AccountUtils() {
    }

    public static boolean isEmpty(TextView textView){
        String text = textView.getText().toString().trim();
        if (TextUtils.isEmpty(text)){
            //获取的文本为空
            return true;
        }else {
            //获取的文本不为空
            return false;
        }
    }

    /**
     *  2017/5/19
     *  描述：通过正则表达式验证是否为手机号码
     *  @return true
     *
     */
    public static boolean isPhone(String account){
        return account.matches("1[3|5|7|8|]\\d{9}");
    }

    public static boolean isEmail(String account){
        return account.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }

}
