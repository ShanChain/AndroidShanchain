package com.shanchain.data.common.ui.widgets;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.common.R;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.utils.ToastUtils;

/**
 * Created by flyye on 2017/9/28.
 */

public class RedPaperDialog extends CustomDialog implements BaseQuickAdapter.OnItemClickListener {
    private String mMsgText;
    private String mTitleText;
    private String mSureText;
    private String mCancelText;
    private Callback sureCallback;
    private Callback cancelCallback;
    private BaseQuickAdapter dialogAdapter;
    private RecyclerView recyclerDialogList;
    private ImageView ImgClose;
    private Button btnSure;


    public RedPaperDialog(Context context) {
        super(context, false, 1.0, R.layout.common_dialog_red_paper, new int[]{R.id.recycler_red_paper_list, R.id.btn_dialog_red_paper_sure, R.id.btn_dialog_red_paper_close});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(mTitleText)) {
            ((TextView) findViewById(R.id.dialog_title)).setText(mTitleText);
        }
        if (!TextUtils.isEmpty(mMsgText)) {
            ((TextView) findViewById(R.id.dialog_msg)).setText(mMsgText);
        }
        ImgClose = findViewById(R.id.btn_dialog_red_paper_close);
        btnSure = findViewById(R.id.btn_dialog_red_paper_sure);
        if (dialogAdapter != null) {
            recyclerDialogList = findViewById(R.id.recycler_red_paper_list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerDialogList.setLayoutManager(linearLayoutManager);
            recyclerDialogList.setAdapter(dialogAdapter);
            dialogAdapter.setOnItemClickListener(this);
        }
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                if (view.getId() == R.id.btn_dialog_red_paper_close) {
                    if (cancelCallback != null) {
                        cancelCallback.invoke();
                    }
                    dismiss();

                } else if (view.getId() == R.id.btn_dialog_red_paper_sure) {
                    if (sureCallback != null) {
                        sureCallback.invoke();
                    }
                    dismiss();
                }
            }
        });

    }

    public void setStandardTitle(String title) {
        mTitleText = title;
    }

    public void setStandardMsg(String msg) {
        mMsgText = msg;
    }

    public void setSureText(String text) {
        mSureText = text;
    }

    public void setCancelText(String text) {
        mCancelText = text;
    }

    public void setDialogAdapter(BaseQuickAdapter adapter) {
        dialogAdapter = adapter;
    }

    public void setCallback(final Callback sureCallback, final Callback cancelCallback) {
        this.sureCallback = sureCallback;
        this.cancelCallback = cancelCallback;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        ToastUtils.showToast(getContext(),"item的点击事件");
    }
}
