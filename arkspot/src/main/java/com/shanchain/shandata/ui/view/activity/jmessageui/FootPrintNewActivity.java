package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.FragmentAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.view.fragment.MainARSGameFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.CouponFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.HomeFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.MineFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.TaskDetailFragment;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.YCommunityFragment;
import com.shanchain.shandata.widgets.BottomTab;
import com.shanchain.shandata.widgets.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by WealChen
 * Date : 2019/7/19
 * Describe :新的首页
 */
public class FootPrintNewActivity extends BaseActivity implements BottomTab.OnTabClickListener{
    @Bind(R.id.viewpager)
    CustomViewPager mViewpager;
    @Bind(R.id.bottom_tab)
    BottomTab bottomTab;

    private FragmentAdapter fragmentAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_foot_print_new;
    }

    @Override
    protected void initViewsAndEvents() {
        initMap();
        initFragment();
    }

    /**
     * 初始化数据
     */
    private void initFragment() {
        mFragmentList.add(CouponFragment.newInstance());
        mFragmentList.add(MainARSGameFragment.newInstance());
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(YCommunityFragment.newInstance());
        mFragmentList.add(MineFragment.newInstance());

        fragmentAdapter=new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.setFragments(mFragmentList);
        mViewpager.setAdapter(fragmentAdapter);
        mViewpager.setOffscreenPageLimit(4);
        bottomTab.changeState(0);
        bottomTab.setOnTabClickListener(this);
        mViewpager.setCurrentItem(0);
        mViewpager.setNoScroll(true);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                bottomTab.changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private long fristTime = 0;

    /**
     * 再按一次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - fristTime > 2000) {
                ToastUtils.showToast(this, getString(R.string.agai_exit_app));
                fristTime = secondTime;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(int position) {
        mViewpager.setCurrentItem(position);
    }
}
