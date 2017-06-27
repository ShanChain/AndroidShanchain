package com.shanchain.shandata.mvp.view.activity.challenge;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.utils.ToastUtils;
import com.shanchain.shandata.widgets.other.CustomSeekBar;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class NoBowResultActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarNoBowResult;
    @Bind(R.id.iv_no_bow_result_icon)
    ImageView mIvNoBowResultIcon;
    @Bind(R.id.tv_no_bow_result)
    TextView mTvNoBowResult;
    @Bind(R.id.iv_no_bow_shanquan)
    ImageView mIvNoBowShanquan;
    @Bind(R.id.tv_no_bow_shanquan)
    TextView mTvNoBowShanquan;
    @Bind(R.id.csb_no_bow_result)
    CustomSeekBar mCsbNoBowResult;
    @Bind(R.id.iv_no_bow_feeling)
    ImageView mIvNoBowFeeling;
    @Bind(R.id.et_no_bow_feeling)
    EditText mEtNoBowFeeling;

    private boolean isSuccess;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_no_bow_result;
    }

    @Override
    protected void initViewsAndEvents() {

        Intent intent = getIntent();
        isSuccess = intent.getBooleanExtra("isSuccess",false);

        initToolBar();
        initData();
    }

    private void initData() {
        mCsbNoBowResult.setProgress(60);
        mCsbNoBowResult.setSeekBarEnable(false);
        mTvNoBowResult.setText(isSuccess?"挑战成功！":"挑战失败！");
    }

    private void initToolBar() {

        mToolbarNoBowResult = (ArthurToolBar) findViewById(R.id.toolbar_no_bow_result);
        mToolbarNoBowResult.setTitleText(isSuccess?"挑战成功":"挑战失败");
        mToolbarNoBowResult.setBtnEnabled(true);
        mToolbarNoBowResult.setBtnVisibility(true);
        mToolbarNoBowResult.setOnLeftClickListener(this);
        mToolbarNoBowResult.setOnRightClickListener(this);
    }

    @OnClick(R.id.iv_no_bow_feeling)
    public void onClick() {
        String feelingContent = mEtNoBowFeeling.getText().toString().trim();
        if (TextUtils.isEmpty(feelingContent)){
            ToastUtils.showToast(this,"快去写点此刻的心情吧~");
        }else {

        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(MainActivity.class);
        ActivityManager.getInstance().finishAllActivity();
    }
}
