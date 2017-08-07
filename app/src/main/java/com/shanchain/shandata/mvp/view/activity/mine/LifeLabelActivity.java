package com.shanchain.shandata.mvp.view.activity.mine;

import android.view.View;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.LifeLabelViewPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import butterknife.Bind;

public class LifeLabelActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    private ArthurToolBar mToolbarLifeLabel;
    @Bind(R.id.hvp_life_label)
    HorizontalInfiniteCycleViewPager mHvpLifeLabel;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_life_label;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
    }

    private void initData() {
        LifeLabelViewPagerAdapter pagerAdapter = new LifeLabelViewPagerAdapter();
        mHvpLifeLabel.setAdapter(pagerAdapter);
    }

    private void initToolBar() {
        mToolbarLifeLabel = (ArthurToolBar) findViewById(R.id.toolbar_life_label);
        mToolbarLifeLabel.setBtnEnabled(true,false);
        mToolbarLifeLabel.setBtnVisibility(true,false);
        mToolbarLifeLabel.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
