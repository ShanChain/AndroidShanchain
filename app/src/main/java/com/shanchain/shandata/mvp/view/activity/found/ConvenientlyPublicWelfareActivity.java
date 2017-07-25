package com.shanchain.shandata.mvp.view.activity.found;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.view.activity.DetailsActivity;
import com.shanchain.shandata.mvp.view.activity.mine.MyPublicWelfareActivity;
import com.shanchain.shandata.mvp.view.activity.mine.ShanCoinsActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ConvenientlyPublicWelfareActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarConvenientlyPublicWelfare;
    @Bind(R.id.tv_conveniently_vouchers)
    TextView mTvConvenientlyVouchers;
    @Bind(R.id.ll_conveniently_vouchers)
    LinearLayout mLlConvenientlyVouchers;
    @Bind(R.id.tv_conveniently_my_welfare)
    TextView mTvConvenientlyMyWelfare;
    @Bind(R.id.ll_conveniently_my_welfare)
    LinearLayout mLlConvenientlyMyWelfare;
    @Bind(R.id.tv_conveniently_wait)
    TextView mTvConvenientlyWait;
    @Bind(R.id.ll_conveniently_my_wait)
    LinearLayout mLlConvenientlyMyWait;
    @Bind(R.id.vp_conveniently)
    ViewPager mVpConveniently;
    @Bind(R.id.tv_conveniently_title)
    TextView mTvConvenientlyTitle;
    @Bind(R.id.tv_conveniently_des)
    TextView mTvConvenientlyDes;
    @Bind(R.id.btn_conveniently_donate)
    Button mBtnConvenientlyDonate;
    @Bind(R.id.iv_conveniently_agency)
    ImageView mIvConvenientlyAgency;
    @Bind(R.id.tv_conveniently_agency)
    TextView mTvConvenientlyAgency;
    @Bind(R.id.tv_conveniently_agency_des)
    TextView mTvConvenientlyAgencyDes;
    @Bind(R.id.ll_conveniently_agency)
    LinearLayout mLlConvenientlyAgency;
    @Bind(R.id.activity_conveniently_public_welfare)
    LinearLayout mActivityConvenientlyPublicWelfare;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_conveniently_public_welfare;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
    }

    private void initToolBar() {
        mToolbarConvenientlyPublicWelfare = (ArthurToolBar) findViewById(R.id.toolbar_conveniently_public_welfare);
        mToolbarConvenientlyPublicWelfare.setBtnEnabled(true,false);
        mToolbarConvenientlyPublicWelfare.setBtnVisibility(true,false);
        mToolbarConvenientlyPublicWelfare.setOnLeftClickListener(this);

    }

    @OnClick({R.id.ll_conveniently_vouchers, R.id.ll_conveniently_my_welfare, R.id.ll_conveniently_my_wait, R.id.btn_conveniently_donate, R.id.ll_conveniently_agency})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_conveniently_vouchers:
                Intent intentVouchers = new Intent(this, ShanCoinsActivity.class);
                intentVouchers.putExtra("isShanCoins",false);
                startActivity(intentVouchers);
                break;
            case R.id.ll_conveniently_my_welfare:
                readyGo(MyPublicWelfareActivity.class);
                break;
            case R.id.ll_conveniently_my_wait:
                readyGo(WaitDonateActivity.class);
                break;
            case R.id.btn_conveniently_donate:
                Intent intentDonate = new Intent(this,PublicWelfareProjectsActivity.class);
                startActivity(intentDonate);
                break;
            case R.id.ll_conveniently_agency:
                readyGo(DetailsActivity.class);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
