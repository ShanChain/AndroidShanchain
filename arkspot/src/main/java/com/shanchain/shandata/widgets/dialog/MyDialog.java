package com.shanchain.shandata.widgets.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

/**
 * Dialog封装
 * Created by hmg on 2016/9/20.
 */
public class MyDialog {
    AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Context context;

    public MyDialog(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
    }

    public MyDialog setTitle(String title) {
        this.builder.setTitle(title);
        return this;
    }

    public MyDialog setMessage(String message) {
        this.builder.setMessage(message);
        return this;
    }

    public MyDialog setMessage(int resId) {
        this.builder.setMessage(context.getString(resId));
        return this;
    }

    public MyDialog setLeftButton(String buttonName, DialogInterface.OnClickListener onClickListener) {
        this.builder.setNegativeButton(buttonName, onClickListener);
        return this;
    }

    public MyDialog setLeftButton(int resId, DialogInterface.OnClickListener onClickListener) {
        this.builder.setNegativeButton(context.getString(resId), onClickListener);
        return this;
    }

    public MyDialog setRightButton(String buttonName, DialogInterface.OnClickListener onClickListener) {
        this.builder.setPositiveButton(buttonName, onClickListener);
        return this;
    }

    public MyDialog setRightButton(int resId, DialogInterface.OnClickListener onClickListener) {
        this.builder.setPositiveButton(context.getString(resId), onClickListener);
        return this;
    }

    public void show() {
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog1) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button button2 = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                button.setTextColor(Color.parseColor("#a260e6"));
                button2.setTextColor(Color.parseColor("#a260e6"));
            }
        });
        dialog.show();
    }

    public AlertDialog create() {
        dialog = builder.create();
        return dialog;
    }

}
