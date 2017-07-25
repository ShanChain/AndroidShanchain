package com.shanchain.shandata.mvp.view.activity.found;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.GlideCircleTransform;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class GoodsDetailsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarGoodsDetails;
    @Bind(R.id.iv_goods_details)
    ImageView mIvGoodsDetails;
    @Bind(R.id.tv_goods_details_goods_name)
    TextView mTvGoodsDetailsGoodsName;
    @Bind(R.id.tv_goods_details_goods_price)
    TextView mTvGoodsDetailsGoodsPrice;
    @Bind(R.id.tv_goods_details_goods_des)
    TextView mTvGoodsDetailsGoodsDes;
    @Bind(R.id.btn_goods_details_exchange)
    Button mBtnGoodsDetailsExchange;
    @Bind(R.id.iv_goods_merchants)
    ImageView mIvGoodsMerchants;
    @Bind(R.id.tv_goods_merchants_name)
    TextView mTvGoodsMerchantsName;
    @Bind(R.id.tv_market_merchants_des)
    TextView mTvMarketMerchantsDes;
    @Bind(R.id.tv_goods_merchants_address)
    TextView mTvGoodsMerchantsAddress;
    @Bind(R.id.tv_goods_merchants_phone)
    TextView mTvGoodsMerchantsPhone;
    @Bind(R.id.activity_goods_details)
    LinearLayout mActivityGoodsDetails;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        Glide.with(this).load(R.drawable.photo_barbecue).transform(new GlideCircleTransform(GoodsDetailsActivity.this)).into(mIvGoodsMerchants);
    }

    private void initToolBar() {
        mToolbarGoodsDetails = (ArthurToolBar) findViewById(R.id.toolbar_goods_details);
        mToolbarGoodsDetails.setBtnEnabled(true,false);
        mToolbarGoodsDetails.setBtnVisibility(true,false);
        mToolbarGoodsDetails.setOnLeftClickListener(this);
    }


    @OnClick(R.id.btn_goods_details_exchange)
    public void onClick() {
        readyGo(ConfirmExchangeActivity.class);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
