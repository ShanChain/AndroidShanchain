package com.shanchain.shandata.ui.view.activity.square;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.MyGroupTeamFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.MyGroupTeamGetFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by WealChen
 * Date : 2019/8/7
 * Describe :我的小分队
 */
public class MyGroupActivity extends BaseActivity {
    @Bind(R.id.tab_layout_main)
    TabLayout mTabLayout;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.vp_main)
    ViewPager mViewPager;
    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.im_search)
    ImageView imSearch;
    private int sourceType = 1;

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
        sourceType = getIntent().getIntExtra("type",1);
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调整搜索界面
                startActivity(new Intent(MyGroupActivity.this,SearchTeamActivity.class));
            }
        });

    }

    private void setFragment() {
        String[] titles = {getString(R.string.can_join_mining), getString(R.string.my_mine)};
        fragmentList.add(MyGroupTeamFragment.getInstance());
        fragmentList.add(MyGroupTeamGetFragment.getInstance());
        TaskPagerAdapter adapter = new TaskPagerAdapter(getSupportFragmentManager(), titles, fragmentList);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        if(sourceType ==1){
            mViewPager.setCurrentItem(0);
        }else {
            mViewPager.setCurrentItem(1);
        }

    }


}
