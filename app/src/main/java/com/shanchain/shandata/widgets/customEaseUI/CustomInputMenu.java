package com.shanchain.shandata.widgets.customEaseUI;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseChatPrimaryMenuBase;
import com.shanchain.shandata.R;

public class CustomInputMenu extends EaseChatPrimaryMenuBase implements View.OnClickListener {

    private EditText mEditText;
    private TextView mTvSend;

    public CustomInputMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomInputMenu(Context context) {
        this(context,null);
    }

    public CustomInputMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initMenu(context);
    }

    private void initMenu(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_input_menu,this);
        mEditText = (EditText) findViewById(R.id.et_input_menu);
        mTvSend = (TextView) findViewById(R.id.tv_send_msg);
        mTvSend.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    mTvSend.setEnabled(false);
                    mTvSend.setBackgroundColor(getResources().getColor(R.color.colorBtnNormal));
                }else {
                     mTvSend.setEnabled(true);
                    mTvSend.setBackgroundColor(getResources().getColor(R.color.colorBtnEnable));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onEmojiconInputEvent(CharSequence emojiContent) {

    }

    @Override
    public void onEmojiconDeleteEvent() {

    }

    @Override
    public void onExtendMenuContainerHide() {

    }

    @Override
    public void onTextInsert(CharSequence text) {
        String s = mEditText.getText().toString();
        mEditText.setText("");

    }

    @Override
    public EditText getEditText() {
        return mEditText;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_send_msg:
                if (listener != null){
                    String s = mEditText.getText().toString();
                    mEditText.setText("");
                    listener.onSendBtnClicked(s);
                }
                break;
            default:

                break;
        }
    }
}
