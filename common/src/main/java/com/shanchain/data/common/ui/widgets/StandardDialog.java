package com.shanchain.data.common.ui.widgets;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shanchain.common.R;
import com.shanchain.data.common.base.Callback;

/**
 * Created by flyye on 2017/9/28.
 */

public class StandardDialog extends CustomDialog{
    private String mMsgText;
    private String mTitleText;
    private String mSureText;
    private String mCancelText;
    private Callback sureCallback;
    private Callback cancelCallback;



    public StandardDialog(Context context){
        super(context, false, 1.0, R.layout.common_dialog_leave_scene, new int[]{R.id.tv_dialog_leave_cancel, R.id.tv_dialog_leave_sure});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!TextUtils.isEmpty(mTitleText)){
            ((TextView) findViewById(R.id.dialog_title)).setText(mTitleText);
        }
        if(!TextUtils.isEmpty(mMsgText)){
            ((TextView) findViewById(R.id.dialog_msg)).setText(mMsgText);
        }
        if(!TextUtils.isEmpty(mSureText)){
            ((TextView) findViewById(R.id.tv_dialog_leave_sure)).setText(mSureText);
        }
        if(!TextUtils.isEmpty(mCancelText)){
            ((TextView) findViewById(R.id.tv_dialog_leave_cancel)).setText(mCancelText);
        }
        this.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                if(view.getId() == R.id.tv_dialog_leave_cancel){
                    if(cancelCallback != null){
                        cancelCallback.invoke();
                    }
                    dismiss();
                }else if(view.getId() == R.id.tv_dialog_leave_sure){
                    if(sureCallback != null){
                        sureCallback.invoke();
                    }
                    dismiss();
                }
            }
        });

    }

    public void setStandardTitle(String title){
        mTitleText = title;
    }
    public void setStandardMsg(String msg){
        mMsgText = msg;
    }
    public void setSureText(String text){
        mSureText = text;
    }
    public void setCancelText(String text){
        mCancelText = text;
    }

    public void setCallback(final Callback sureCallback,final Callback cancelCallback){
        this.sureCallback = sureCallback;
        this.cancelCallback = cancelCallback;
    }
}
