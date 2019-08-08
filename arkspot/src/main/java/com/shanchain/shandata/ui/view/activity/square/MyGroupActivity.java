package com.shanchain.shandata.ui.view.activity.square;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.MyGroupTeamFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.MyGroupTeamGetFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WealChen
 * Date : 2019/8/7
 * Describe :我的小分队
 */
public class MyGroupActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener{
    @Bind(R.id.tb_register)
    ArthurToolBar mTbRegister;
    @Bind(R.id.tab_layout_main)
    TabLayout mTabLayout;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.vp_main)
    ViewPager mViewPager;

    private List<Fragment> fragmentList = new ArrayList();
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_mygroup;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        setFragment();
    }

    private void initToolBar() {
        mTbRegister.setTitleTextColor(Color.BLACK);
        mTbRegister.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        mTbRegister.setOnLeftClickListener(this);
        mTbRegister.setTitleText(getString(R.string.mining_area));

    }

    private void setFragment() {
        String[] titles = {getString(R.string.can_join_mining),getString(R.string.my_mine)};
        fragmentList.add(MyGroupTeamFragment.getInstance());
        fragmentList.add(MyGroupTeamGetFragment.getInstance());
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
