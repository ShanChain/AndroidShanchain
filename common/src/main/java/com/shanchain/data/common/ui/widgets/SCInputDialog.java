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

/**
 * Created by zhoujian on 2017/10/21.
 */

public class SCInputDialog extends AlertDialog {

    private String mTitle;
    private String mPlaceHolder;
    private EditText mEtContent;
    private Callback sureCallback;
    private Callback cancelCallback;

    public SCInputDialog(@NonNull Context context,String title,String placeHolder) {
        super(context);
        this.mTitle = title;
        this.mPlaceHolder = placeHolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_input);
        TextView tvTitle = (TextView) findViewById(R.id.tv_input_dialog_title);
        mEtContent = (EditText) findViewById(R.id.et_input_dialog_content);
        Button btnCancel = (Button) findViewById(R.id.btn_dialog_input_cancel);
        Button btnSure = (Button) findViewById(R.id.btn_dialog_input_sure);

        tvTitle.setText(mTitle);
        mEtContent.setHint(mPlaceHolder);

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sureCallback != null){
                    sureCallback.invoke();
                }
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelCallback!=null){
                    cancelCallback.invoke();
                }
                dismiss();
            }
        });

    }

    public String getInputContent() {
        return mEtContent.getText().toString().trim();
    }

    public void setCallback(final Callback sureCallback, final Callback cancelCallback){
        this.sureCallback = sureCallback;
        this.cancelCallback = cancelCallback;
    }


}
