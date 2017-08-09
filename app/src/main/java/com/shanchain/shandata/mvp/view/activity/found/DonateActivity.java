package com.shanchain.shandata.mvp.view.activity.found;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.LogUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class DonateActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarDonate;
    @Bind(R.id.tv_donate_title)
    TextView mTvDonateTitle;
    @Bind(R.id.iv_donate_img)
    ImageView mIvDonateImg;
    @Bind(R.id.iv_donate_increase)
    ImageView mIvDonateIncrease;
    @Bind(R.id.iv_donate_cutback)
    ImageView mIvDonateCutback;
    @Bind(R.id.tv_donate_remaining)
    TextView mTvDonateRemaining;
    @Bind(R.id.btn_donate_sure)
    Button mBtnDonateSure;
    @Bind(R.id.activity_donate)
    LinearLayout mActivityDonate;
    @Bind(R.id.et_donate_counts)
    EditText mEtDonateCounts;
    private String mCounts;
    private int mVouchers;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_donate;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarDonate = (ArthurToolBar) findViewById(R.id.toolbar_donate);
        mToolbarDonate.setBtnEnabled(true, false);
        mToolbarDonate.setBtnVisibility(true, false);
        mToolbarDonate.setOnLeftClickListener(this);

    }

    @OnClick({R.id.iv_donate_increase, R.id.iv_donate_cutback, R.id.btn_donate_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_donate_increase:
                getCountOfVouchers();
                mVouchers ++;
                mEtDonateCounts.setText(mVouchers + "");
                break;
            case R.id.iv_donate_cutback:
                getCountOfVouchers();
                mVouchers --;
                mEtDonateCounts.setText(mVouchers + "");
                break;
            case R.id.btn_donate_sure:
                getCountOfVouchers();
                LogUtils.d("捐赠了" + mVouchers);
                readyGo(DonateSuccessActivity.class);
                break;
        }
    }

    private void getCountOfVouchers() {
        mCounts = mEtDonateCounts.getText().toString().trim();
        mVouchers = Integer.parseInt(mCounts);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
