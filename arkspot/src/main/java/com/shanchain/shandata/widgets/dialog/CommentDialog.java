package com.shanchain.shandata.widgets.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.utils.KeyboardUtils;
import com.shanchain.shandata.widgets.listener.SCTextWatcher;

/**
 * Created by zhoujian on 2017/11/28.
 */

public class CommentDialog extends DialogFragment implements View.OnClickListener {

    private EditText mEtInput;
    private TextView mTvCommentSend;
    private TextView mTvOutSide;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让dialogFragment弹出时沾满全屏
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pop_comment, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDialogBg)));
        initView(view);
        return view;
    }


    private void initView(View view) {
        mEtInput = (EditText) view.findViewById(R.id.et_pop_comment);
        mTvCommentSend = (TextView) view.findViewById(R.id.tv_pop_comment_send);
        mTvOutSide = (TextView) view.findViewById(R.id.tv_pop_comment_outside);
        mTvCommentSend.setOnClickListener(this);
        mTvOutSide.setOnClickListener(this);
        mEtInput.addTextChangedListener(new SCTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)){
                    mTvCommentSend.setEnabled(false);
                }else {
                    mTvCommentSend.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_pop_comment_outside) {
        } else if (v.getId() == R.id.tv_pop_comment_send) {
            String comment = mEtInput.getText().toString().trim();
            if (TextUtils.isEmpty(comment)) {
                ToastUtils.showToast(v.getContext(), "不能发布空评论");
            } else {
                mListener.onSendClick(v,comment);
            }

        }
        KeyboardUtils.hideSoftInput(v);
        dismiss();
    }

    public interface OnSendClickListener{
        void onSendClick(View v ,String msg);
    }

    public OnSendClickListener mListener;

    public void setOnSendClickListener(OnSendClickListener listener){
        mListener = listener;
    }
}
