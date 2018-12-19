package com.shanchain.shandata.ui.view.activity.coupon;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.CouponInfo;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.List;

public class MyCouponListActivity extends BaseActivity implements ArthurToolBar.OnRightClickListener,ArthurToolBar.OnLeftClickListener {
    private ArthurToolBar toolBar;
    private ViewPager vpCoupon;
    private TabLayout tabLayoutCoupon;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_coupon_list;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initView();
        initData();

    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            CouponInfo couponInfo = new CouponInfo();
            couponInfo.setName("肯定基饭店" + i);
            couponInfo.setPrice("$" + i);
            couponInfo.setUserStatus("领取" + i);
            couponInfo.setRemainAmount("剩余" + i);
//            couponInfoList.add(couponInfo);
        }
    }

    private void initView() {
        String[] titles = {"我领取的", "我创建的"};
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles);
        vpCoupon = findViewById(R.id.vp_coupon);
        tabLayoutCoupon = findViewById(R.id.tab_coupon);
        vpCoupon.setOffscreenPageLimit(2);
        vpCoupon.setAdapter(adapter);
        tabLayoutCoupon.setupWithViewPager(vpCoupon);
        vpCoupon.setCurrentItem(0);

    }

    private void initToolBar() {
        toolBar = findViewById(R.id.tb_main);
        toolBar.setTitleText("我的马甲劵");

        toolBar.setOnLeftClickListener(this);//左侧导航栏监听
        toolBar.setOnRightClickListener(this);//右侧导航栏监听
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }
}
