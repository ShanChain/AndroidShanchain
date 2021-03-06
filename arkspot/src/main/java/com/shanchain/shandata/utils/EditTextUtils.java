package com.shanchain.shandata.utils;

import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by zhoujian on 2017/11/21.
 */

public class EditTextUtils {

    private EditTextUtils(){}

    /**
     *  描述：禁止输入框换行
     *  @param et 被禁止换行的edittext
     */
    public static void banEnterInput(EditText et){
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return true;
            }
        });
    }

}
