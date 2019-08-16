package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class FootPrintNewActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.viewpager)
    CustomViewPager mViewpager;
    /*@Bind(R.id.bottom_tab)
    BottomTab bottomTab;*/
    private LinearLayout ll0;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private LinearLayout ll4;
    private ImageView iv0;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private TextView tv0;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    private FragmentAdapter fragmentAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_foot_print_new;
    }

    @Override
    protected void initViewsAndEvents() {
        initMap();
        initViews();
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
        changeState(2);
        mViewpager.setCurrentItem(2);
        /*bottomTab.changeState(0);
        bottomTab.setOnTabClickListener(this);*/
        mViewpager.setNoScroll(true);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
//                bottomTab.changeState(position);
                changeState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //初始化view
    private void initViews() {
        ll0= (LinearLayout) findViewById(R.id.ll_shouye);
        ll1 = (LinearLayout) findViewById(R.id.ll_bankuai);
        ll2 = (LinearLayout) findViewById(R.id.ll_cause);
        ll3 = (LinearLayout) findViewById(R.id.ll_squre);
        ll4 = (LinearLayout) findViewById(R.id.ll_news);
        iv0 = (ImageView) findViewById(R.id.iv_shouye);
        iv1 = (ImageView) findViewById(R.id.iv_bankuai);
        iv2 = (ImageView) findViewById(R.id.iv_cause);
        iv3 = (ImageView) findViewById(R.id.iv_squre);
        iv4 = (ImageView) findViewById(R.id.iv_news);
        tv0 = (TextView) findViewById(R.id.tv_shouye);
        tv1 = (TextView) findViewById(R.id.tv_bankuai);
        tv2 = (TextView) findViewById(R.id.tv_cause);
        tv3 = (TextView) findViewById(R.id.tv_squre);
        tv4 = (TextView) findViewById(R.id.tv_news);

        initListener();
    }

    private void initListener() {
        ll0.setOnClickListener(this);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);
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

    /*@Override
    public void onItemClick(int position) {
        mViewpager.setCurrentItem(position);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shouye:
                /*changeState(0);
                mViewpager.setCurrentItem(0);*/
                ToastUtils.showToast(this,"该功能暂未开放，敬请期待");
                break;
            case R.id.ll_bankuai:
                changeState(1);
                mViewpager.setCurrentItem(1);
                break;
            case R.id.ll_cause:
                changeState(2);
                mViewpager.setCurrentItem(2);
                break;
            case R.id.ll_squre:
                changeState(3);
                mViewpager.setCurrentItem(3);
                break;
            case R.id.ll_news:
                changeState(4);
                mViewpager.setCurrentItem(4);
                break;
        }
    }
    public void changeState(int position) {
        tv0.setTextColor(getResources().getColor(R.color.white_btn_press));
        tv1.setTextColor(getResources().getColor(R.color.white_btn_press));
        tv2.setTextColor(getResources().getColor(R.color.white_btn_press));
        tv3.setTextColor(getResources().getColor(R.color.white_btn_press));
        tv4.setTextColor(getResources().getColor(R.color.white_btn_press));
        iv0.setImageResource(R.mipmap.maya_c);
        iv1.setImageResource(R.mipmap.sheqb_c);
        iv2.setImageResource(R.mipmap.midle_icon);
        iv3.setImageResource(R.mipmap.yuansq_c);
        iv4.setImageResource(R.mipmap.mine_c);
        switch (position) {
            case 0:
                iv0.setImageResource(R.mipmap.majia_s);
                tv0.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 1:
                iv1.setImageResource(R.mipmap.sheqb_s);
                tv1.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 2:
//                iv2.setImageResource(R.mipmap.sheqb_s);
                tv2.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 3:
                iv3.setImageResource(R.mipmap.yuansq_s);
                tv3.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 4:
                iv4.setImageResource(R.mipmap.mine_s);
                tv4.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;

        }
    }
}
