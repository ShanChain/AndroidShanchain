package com.shanchain.shandata.mvp.view.activity.found;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.utils.SpanUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;
import butterknife.OnClick;

public class ConfirmExchangeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarConfirmExchange;
    @Bind(R.id.iv_exchange_goods)
    ImageView mIvExchangeGoods;
    @Bind(R.id.tv_exchange_goods_name)
    TextView mTvExchangeGoodsName;
    @Bind(R.id.btn_exchange_goods_add)
    Button mBtnExchangeGoodsAdd;
    @Bind(R.id.tv_exchange_goods_counts)
    TextView mTvExchangeGoodsCounts;
    @Bind(R.id.btn_exchange_goods_subtract)
    Button mBtnExchangeGoodsSubtract;
    @Bind(R.id.btn_exchange_goods_commit)
    Button mBtnExchangeGoodsCommit;
    @Bind(R.id.activity_confirm_exchange)
    LinearLayout mActivityConfirmExchange;
    @Bind(R.id.tv_exchange_total)
    TextView mTvExchangeTotal;

    private double price;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_confirm_exchange;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        price = 0.5;
        mTvExchangeTotal.setText(SpanUtils.buildSpannableString("合计: " + 1 * price + "善圆",getResources().getColor(R.color.colorBtn),0,3));
    }

    private void initToolBar() {
        mToolbarConfirmExchange = (ArthurToolBar) findViewById(R.id.toolbar_confirm_exchange);
        mToolbarConfirmExchange.setBtnEnabled(true, false);
        mToolbarConfirmExchange.setBtnVisibility(true, false);
        mToolbarConfirmExchange.setOnLeftClickListener(this);
    }

    @OnClick({R.id.btn_exchange_goods_add, R.id.btn_exchange_goods_subtract, R.id.btn_exchange_goods_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exchange_goods_add:
                String counts = mTvExchangeGoodsCounts.getText().toString().trim();
                int goodsCounts = Integer.parseInt(counts);
                goodsCounts++;
                if (goodsCounts > 50) {
                    goodsCounts = 50;
                }
                mTvExchangeGoodsCounts.setText(goodsCounts + "");
                mTvExchangeTotal.setText(SpanUtils.buildSpannableString("合计: " + goodsCounts * price + "善圆",getResources().getColor(R.color.colorBtn),0,3));
                break;
            case R.id.btn_exchange_goods_subtract:
                String count = mTvExchangeGoodsCounts.getText().toString().trim();
                int goodsCount = Integer.parseInt(count);
                goodsCount--;
                if (goodsCount < 1) {
                    goodsCount = 1;
                }
                mTvExchangeGoodsCounts.setText(goodsCount + "");
                mTvExchangeTotal.setText(SpanUtils.buildSpannableString("合计: " + goodsCount * price + "善圆",getResources().getColor(R.color.colorBtn),0,3));
                break;
            case R.id.btn_exchange_goods_commit:
                String confirmCounts = mTvExchangeGoodsCounts.getText().toString().trim();
                int confirmCount = Integer.parseInt(confirmCounts);
                Intent intent = new Intent(this,ExchangeCodeActivity.class);
                intent.putExtra("goodsName","青岛啤酒");
                intent.putExtra("goodsCounts",confirmCount);
                intent.putExtra("goodsTotal",confirmCount*price);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

}
