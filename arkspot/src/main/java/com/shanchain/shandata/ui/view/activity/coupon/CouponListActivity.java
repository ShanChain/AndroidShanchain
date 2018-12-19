package com.shanchain.shandata.ui.view.activity.coupon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class CouponListActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener,ArthurToolBar.OnRightClickListener {
    private ArthurToolBar toolBar;
    private LinearLayout addCoupon;
    private RecyclerView recyclerView;
    private BGARefreshLayout refreshLayout;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_coupon_list;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initView();
    }

    private void initView() {
        refreshLayout = findViewById(R.id.refresh_layout);
        addCoupon = findViewById(R.id.linear_add_coupon);
        recyclerView = findViewById(R.id.recycler_view_coupon);



    }

    private void initToolBar() {
        toolBar = findViewById(R.id.tb_coupon);
        toolBar.setTitleText("马甲卷");
        toolBar.setRightText("我的");
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }
}
