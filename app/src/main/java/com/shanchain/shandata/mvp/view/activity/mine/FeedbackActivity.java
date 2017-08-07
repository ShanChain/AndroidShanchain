package com.shanchain.shandata.mvp.view.activity.mine;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class FeedbackActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    ArthurToolBar mToolbarFeedBack;
    @Bind(R.id.et_feedback)
    EditText mEtFeedback;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarFeedBack = (ArthurToolBar) findViewById(R.id.toolbar_feed_back);
        mToolbarFeedBack.setBtnVisibility(true);
        mToolbarFeedBack.setBtnEnabled(true);
        mToolbarFeedBack.setOnLeftClickListener(this);
        mToolbarFeedBack.setOnRightClickListener(this);

    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        String feedback = mEtFeedback.getText().toString().trim();
        if (TextUtils.isEmpty(feedback)){
            ToastUtils.showToast(this,"总得写点东西吧！亲~");
        }else {
            //提交服务器
            showLoadingDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    closeLoadingDialog();
                }
            },1000);
        }
    }
}
