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
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class WalkMoreResultActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarWalkMoreResult;
    @Bind(R.id.iv_walk_result_icon)
    ImageView mIvWalkResultIcon;
    @Bind(R.id.tv_walk_result)
    TextView mTvWalkResult;
    @Bind(R.id.iv_walk_result_img_mine)
    ImageView mIvWalkResultImgMine;
    @Bind(R.id.tv_walk_result_confidence_mine)
    TextView mTvWalkResultConfidenceMine;
    @Bind(R.id.tv_walk_result_shanquan_mine)
    TextView mTvWalkResultShanquanMine;
    @Bind(R.id.iv_walk_result_img_rival)
    ImageView mIvWalkResultImgRival;
    @Bind(R.id.tv_walk_result_name_rival)
    TextView mTvWalkResultNameRival;
    @Bind(R.id.tv_walk_result_confidence_rival)
    TextView mTvWalkResultConfidenceRival;
    @Bind(R.id.tv_walk_result_shanquan_rival)
    TextView mTvWalkResultShanquanRival;
    @Bind(R.id.iv_walk_result_feeling)
    ImageView mIvWalkResultFeeling;
    @Bind(R.id.et_walk_result_feeling)
    EditText mEtWalkResultFeeling;

    private boolean isSuccess;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_walk_more_result;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        isSuccess = intent.getBooleanExtra("isSuccess", false);
        initData();
        initToolBar();
    }

    private void initData() {
        mTvWalkResult.setText(isSuccess ? "挑战成功！" : "挑战失败！");
    }

    private void initToolBar() {
        mToolbarWalkMoreResult = (ArthurToolBar) findViewById(R.id.toolbar_walk_more_result);
        mToolbarWalkMoreResult.setTitleText(isSuccess ? "挑战成功" : "挑战失败");
        mToolbarWalkMoreResult.setBtnEnabled(true);
        mToolbarWalkMoreResult.setBtnVisibility(true);
        mToolbarWalkMoreResult.setOnLeftClickListener(this);
        mToolbarWalkMoreResult.setOnRightClickListener(this);
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

    @OnClick(R.id.iv_walk_result_feeling)
    public void onClick() {
        String feelingContent = mEtWalkResultFeeling.getText().toString().trim();
        if (TextUtils.isEmpty(feelingContent)) {
            ToastUtils.showToast(this, "快去写点此刻的心情吧~");
        } else {

        }
    }
}
