package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanchain.data.common.base.ActivityStackManager;
import com.shanchain.data.common.base.Callback;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.ui.widgets.StandardDialog;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.PrefUtils;
import com.shanchain.data.common.utils.ThreadUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.FragmentAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.MyApplication;
import com.shanchain.shandata.ui.view.activity.login.LoginActivity;
import com.shanchain.shandata.ui.view.fragment.MainARSGameFragment;
import com.shanchain.shandata.ui.view.fragment.STOFragment;
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
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by WealChen
 * Date : 2019/7/19
 * Describe :新的首页
 */
public class FootPrintNewActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.viewpager)
    CustomViewPager mViewpager;
    @Bind(R.id.rl_tishi)
    RelativeLayout rlTishi;
    @Bind(R.id.rl_view1)
    RelativeLayout rlView1;
    @Bind(R.id.rl_view2)
    RelativeLayout rlView2;
    @Bind(R.id.rl_view3)
    RelativeLayout rlView3;
    @Bind(R.id.tv_join)
    TextView tvJoin;
    @Bind(R.id.rl_home_intro)
    RelativeLayout rlView0;
    @Bind(R.id.im_first)
    ImageView imFirst;
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
    private int sourceType = 0;
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_foot_print_new;
    }

    @Override
    protected void initViewsAndEvents() {
        sourceType = getIntent().getIntExtra("type",0);
        initMap();
        initViews();
        initFragment();

        initGaidView();
    }
    //显示引导页提示
    private void initGaidView() {
        boolean guided = PrefUtils.getBoolean(mContext, Constants.SP_KEY_GUIDE_VIEW, false);
        if(!guided){
            rlTishi.setVisibility(View.VISIBLE);
            if("zh".equals(MyApplication.getInstance().getSystemLanguge())){
                imFirst.setBackgroundResource(R.mipmap.home_page_intro_1);
            }else {
                imFirst.setBackgroundResource(R.mipmap.home_page_intro_2);
            }
            PrefUtils.putBoolean(mContext, Constants.SP_KEY_GUIDE_VIEW,true);
        }else {
            rlTishi.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    //引导页介绍页第一张显示点击
    @OnClick(R.id.rl_home_intro)
    void viewHome(){
        rlView1.setVisibility(View.VISIBLE);
        rlView3.setVisibility(View.GONE);
        rlView2.setVisibility(View.GONE);
        rlView0.setVisibility(View.GONE);
    }
    //引导页第一张显示点击
    @OnClick(R.id.rl_view1)
    void view1(){
        rlView1.setVisibility(View.GONE);
        rlView3.setVisibility(View.GONE);
        rlView2.setVisibility(View.VISIBLE);
        rlView0.setVisibility(View.GONE);
    }
    //引导页第二张显示点击
    @OnClick(R.id.rl_view2)
    void view2(){
        /*rlView1.setVisibility(View.GONE);
        rlView2.setVisibility(View.GONE);
        rlView3.setVisibility(View.VISIBLE);
        rlView0.setVisibility(View.GONE);*/
        PrefUtils.putBoolean(mContext, Constants.SP_KEY_GUIDE_VIEW,true);
        rlTishi.setVisibility(View.GONE);
    }
    //立即进入
    @OnClick({R.id.tv_join,R.id.rl_view3})
    void view3(){


    }

    /**
     * 初始化数据
     */
    private void initFragment() {
        mFragmentList.add(CouponFragment.newInstance());
        mFragmentList.add(STOFragment.newInstance());
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
        //极光用户登录状态监听
        JMessageClient.registerEventReceiver(this);
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
                changeState(0);
                mViewpager.setCurrentItem(0);
//                ToastUtils.showToast(this,"该功能暂未开放，敬请期待");
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
        iv1.setImageResource(R.mipmap.nianlunz_c);
        iv2.setImageResource(R.mipmap.midle_icon_c);
        iv3.setImageResource(R.mipmap.squre_c);
        iv4.setImageResource(R.mipmap.mine_new_c);
        switch (position) {
            case 0:
                iv0.setImageResource(R.mipmap.majia_s);
                tv0.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 1:
                iv1.setImageResource(R.mipmap.nianlunz_s);
                tv1.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 2:
                iv2.setImageResource(R.mipmap.midle_icon);
                tv2.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 3:
                iv3.setImageResource(R.mipmap.square_s);
                tv3.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;
            case 4:
                iv4.setImageResource(R.mipmap.mine_new_s);
                tv4.setTextColor(getResources().getColor(R.color.login_marjar_color));
                break;

        }
    }

    //监听用户登录状态
    public void onEventMainThread(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();//获取变更的原因
        UserInfo myInfo = event.getMyInfo();//获取当前被登出账号的信息
        switch (reason) {
            case user_password_change:
                //用户密码在服务器端被修改
                LogUtils.d("LoginStateChangeEvent", "用户密码在服务器端被修改");
//                ToastUtils.showToast(mContext, "您的密码已被修改");
                break;
            case user_logout:
                //用户换设备登录
                LogUtils.d("LoginStateChangeEvent", "账号在其他设备上登录");
                final StandardDialog standardDialog = new StandardDialog(FootPrintNewActivity.this);
                standardDialog.setStandardTitle(getString(R.string.prompt));
                standardDialog.setStandardMsg(getString(R.string.account_other_login));
                standardDialog.setSureText(getString(R.string.re_login));
                standardDialog.setCancelText(getString(R.string.str_cancel));
                standardDialog.setCallback(new Callback() {//确定
                    @Override
                    public void invoke() {
                        loginOut();
                    }
                }, new Callback() {//取消
                    @Override
                    public void invoke() {
                        loginOut();
                    }
                });
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtils.showToast(getApplicationContext(), "账号在其他设备上登录");
                        standardDialog.show();
                    }
                });
                standardDialog.setCanceledOnTouchOutside(false);
                break;
            case user_deleted:
                //用户被删除
                break;
        }
    }

    @Override
    protected void onDestroy() {
        JMessageClient.unRegisterEventReceiver(this);
        super.onDestroy();
    }
}
