package com.shanchain.data.common.ui.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shanchain.common.R;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.utils.ToastUtils;

/**
 * Created by zhoujian on 2017/10/21.
 */

public class SCInputDialog{

    private String mTitle;
    private String mPlaceHolder;
    private EditText mEtContent;
    private Callback sureCallback;
    private Callback cancelCallback;
    private AlertDialog mAlertDialog;
    private Context mContext;

    public SCInputDialog(@NonNull Context context,String title,String placeHolder) {
        this.mTitle = title;
        this.mPlaceHolder = placeHolder;
        mAlertDialog = new AlertDialog.Builder(context).create();
        mContext = context;
    }
    public void show(){
        View view = View.inflate(mContext, R.layout.common_dialog_input, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_input_dialog_title);
        mEtContent = (EditText) view.findViewById(R.id.et_input_dialog_content);
        Button btnCancel = (Button) view.findViewById(R.id.btn_dialog_input_cancel);
        Button btnSure = (Button) view.findViewById(R.id.btn_dialog_input_sure);

        tvTitle.setText(mTitle);
        mEtContent.setHint(mPlaceHolder);

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureCallback != null){
                    sureCallback.invoke();
                }
                if(mAlertDialog != null){
                    mAlertDialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelCallback!=null){
                    cancelCallback.invoke();
                }
                if(mAlertDialog != null){
                    mAlertDialog.dismiss();
                }
            }
        });
        mAlertDialog.setView(view);
        mAlertDialog.setCancelable(true);
        mAlertDialog.show();
    }
    public void dismiss(){
        if(mAlertDialog != null){
            mAlertDialog.dismiss();
        }
    }
    public String getInputContent() {
        return mEtContent.getText().toString().trim();
    }

    public void setCallback(final Callback sureCallback, final Callback cancelCallback){
        this.sureCallback = sureCallback;
        this.cancelCallback = cancelCallback;
    }


}
