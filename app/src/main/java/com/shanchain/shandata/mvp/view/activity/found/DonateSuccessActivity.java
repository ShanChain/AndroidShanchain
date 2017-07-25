package com.shanchain.shandata.mvp.view.activity.found;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class DonateSuccessActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener, ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarDonateSuccess;
    @Bind(R.id.tv_donate_success_title)
    TextView mTvDonateSuccessTitle;
    @Bind(R.id.tv_donate_success_des)
    TextView mTvDonateSuccessDes;
    @Bind(R.id.activity_donate_success)
    LinearLayout mActivityDonateSuccess;
    @Bind(R.id.iv_donate_success)
    ImageView mIvDonateSuccess;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_donate_success;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {

    }

    private void initToolBar() {
        mToolbarDonateSuccess = (ArthurToolBar) findViewById(R.id.toolbar_donate_success);
        mToolbarDonateSuccess.setOnLeftClickListener(this);
        mToolbarDonateSuccess.setOnRightClickListener(this);
    }

    @Override
    public void onRightClick(View v) {
        ActivityManager.getInstance().finishActivity(DonateActivity.class);
        ActivityManager.getInstance().finishActivity(PublicWelfareProjectsActivity.class);
        ActivityManager.getInstance().finishActivity(WaitDonateActivity.class);
        finish();
    }

    @Override
    public void onLeftClick(View v) {

        finish();
    }

}
