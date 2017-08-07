package com.shanchain.shandata.mvp.view.activity.found;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.manager.ActivityManager;
import com.shanchain.shandata.mvp.view.activity.MainActivity;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.utils.QrCodeUtils;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.Random;

import butterknife.Bind;

public class ExchangeCodeActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    ArthurToolBar mToolbarExchangeCode;
    @Bind(R.id.tv_exchange_code)
    TextView mTvExchangeCode;
    @Bind(R.id.iv_exchange_code)
    ImageView mIvExchangeCode;
    @Bind(R.id.activity_exchange_code)
    LinearLayout mActivityExchangeCode;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_exchange_code;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();

    }

    private void initData() {
        int goodsCounts = getIntent().getIntExtra("goodsCounts", 0);
        double goodsTotal = getIntent().getDoubleExtra("goodsTotal", 0);
        String goodsName = getIntent().getStringExtra("goodsName");

        Bitmap qrImage = QrCodeUtils.createQRImage(goodsName  + "; 数量: " +goodsCounts+  "; 总价: " + goodsTotal, DensityUtils.dip2px(this, 115), DensityUtils.dip2px(this, 115));
        mIvExchangeCode.setImageBitmap(qrImage);
        mTvExchangeCode.setText("兑换码: " + new Random().nextInt(60000) * 254);
    }

    private void initToolBar() {
        mToolbarExchangeCode = (ArthurToolBar) findViewById(R.id.toolbar_exchange_code);
        mToolbarExchangeCode.setBtnEnabled(true);
        mToolbarExchangeCode.setBtnVisibility(true);
        mToolbarExchangeCode.setOnLeftClickListener(this);
        mToolbarExchangeCode.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        ActivityManager.getInstance().finishToActivity(MainActivity.class, true);
    }
}
