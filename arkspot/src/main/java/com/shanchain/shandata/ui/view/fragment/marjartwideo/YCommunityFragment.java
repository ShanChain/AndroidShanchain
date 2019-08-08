package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskPagerAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.interfaces.IUpdateUserHeadCallback;
import com.shanchain.shandata.ui.model.CharacterInfo;
import com.shanchain.shandata.ui.model.JmAccount;
import com.shanchain.shandata.ui.model.ModifyUserInfo;
import com.shanchain.shandata.ui.view.activity.HomeActivity;
import com.shanchain.shandata.ui.view.activity.coupon.MyCouponListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MyMessageActivity;
import com.shanchain.shandata.ui.view.activity.settings.SettingsActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.ui.view.fragment.MainARSGameFragment;
import com.shanchain.shandata.ui.view.fragment.MainChatRoomFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.imui.view.CircleImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by WealChen
 * Date : 2019/7/19
 * Describe :元社区
 */
public class YCommunityFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener{
    @Bind(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.im_add_community)
    ImageView imAddCommunity;
    @Bind(R.id.tab_layout_main)
    TabLayout mTabLayout;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.vp_main)
    ViewPager mViewPager;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    private TextView userNikeView;
    private TextView tvUserSign;
    private ImageView ivUserModify;
    private ImageView userHeadView;
    private ActionBarDrawerToggle toggle;
    private UserInfo mMyInfo;
    private String TAG = "YCommunityFragment";
    private List<Fragment> fragmentList = new ArrayList();
    private MyBroadcastReceiver mReceiver;
    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_yuan_comunity, null);
    }

    public static YCommunityFragment newInstance() {
        YCommunityFragment fragment = new YCommunityFragment();
        return fragment;
    }


    //定义接受头像变更的广播
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //这里的intent可以获取发送广播时传入的数据
            final String urlPath = intent.getStringExtra("url");
            if(!TextUtils.isEmpty(urlPath)){
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity())
                                .load(urlPath)
                                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                        .error(R.drawable.aurora_headicon_default))
                                .into(ivUserHead);
                        Glide.with(getActivity())
                                .load(urlPath)
                                .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                                        .error(R.drawable.aurora_headicon_default))
                                .into(userHeadView);
                        /*ivUserHead.invalidate();
                        userHeadView.invalidate();*/
                    }
                });
            }

        }
    }
    //注册广播
    private void registerBroadcase(){
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.my.sentborad");
        getActivity().registerReceiver(mReceiver, filter);
    }


    @Override
    public void initData() {
        initDrawer();
        setFragment();
        registerBroadcase();
    }

    //创建元社区
    @OnClick(R.id.im_add_community)
    void addComunity(){
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("isAddRoom", true);
        startActivity(intent);
    }

    //点击头像打开侧滑布局
    @OnClick(R.id.iv_user_head)
    void openLeftLayout(){
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        toggle.onDrawerOpened(drawer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }

    //初始化侧滑栏
    private void initDrawer() {
        toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawer.setStatusBarBackground(R.drawable.selector_bg_msg_send_theme);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headView = navigationView.getHeaderView(0);
        userNikeView = headView.findViewById(R.id.tv_nike_name);//用户昵称
        tvUserSign = headView.findViewById(R.id.tv_user_sign);//用户签名
        ivUserModify = headView.findViewById(R.id.iv_user_modify);//修改资料按钮
        userHeadView = headView.findViewById(R.id.iv_user_head);//用户头像

        initUserInfo();
    }

    //初始化用户信息
    private void initUserInfo() {
        mMyInfo = JMessageClient.getMyInfo();
        if(null != mMyInfo){
            userNikeView.setText("" + mMyInfo.getNickname());
            tvUserSign.setText(mMyInfo.getSignature());
            mMyInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                @Override
                public void gotResult(int responseCode, String responseMessage, Bitmap avatarBitmap) {
                    LogUtils.d(TAG,"get Jushinfo responseMessage:"+responseMessage+",responseCode: "+responseCode);
                    if (responseCode == 0) {
                        userHeadView.setImageBitmap(avatarBitmap);
                        ivUserHead.setImageBitmap(avatarBitmap);
                    } else {
                        userHeadView.setImageResource(R.mipmap.aurora_headicon_default);
                        ivUserHead.setImageResource(R.mipmap.aurora_headicon_default);
                    }
                }
            });
        }else {
            CharacterInfo characterInfo = JSONObject.parseObject(SCCacheUtils.getCacheCharacterInfo(), CharacterInfo.class);
            if (characterInfo != null && characterInfo.getCharacterId() != 0) {
                String nikeName = characterInfo.getName() != null ? characterInfo.getName() : "";
                String signature = characterInfo.getSignature() != null ? characterInfo.getSignature() : "";
                final String headImg = characterInfo.getHeadImg() != null ? characterInfo.getHeadImg() : "";
                if (!TextUtils.isEmpty(nikeName)) {
                    userNikeView.setText(nikeName);
                }
                if (!TextUtils.isEmpty(signature)) {
                    tvUserSign.setText(signature);
                }
                if (!TextUtils.isEmpty(headImg)) {
                    RequestOptions options = new RequestOptions();
                    options.placeholder(R.mipmap.aurora_headicon_default);
                    Glide.with(getActivity()).load(headImg).apply(options).into(userHeadView);
                    Glide.with(getActivity()).load(headImg).apply(options).into(ivUserHead);
                }
            }
        }
    }

    private void setFragment() {
//        String[] titles = {getResources().getString(R.string.hot_meta), getResources().getString(R.string.square),"ARS",};
        String[] titles = { getResources().getString(R.string.square)};
//        fragmentList.add(new MainChatRoomFragment());
        fragmentList.add(new SquareFragment());
        fragmentList.add(new MainARSGameFragment());
        TaskPagerAdapter adapter = new TaskPagerAdapter(getChildFragmentManager(), titles, fragmentList);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.nav_my_wallet://我的钱包
                intent = new Intent(getActivity(), com.shanchain.shandata.rn.activity.SCWebViewActivity.class);
                JSONObject obj = new JSONObject();
                obj.put("url", HttpApi.SEAT_WALLET);
                obj.put("title", getResources().getString(R.string.nav_my_wallet));
                String webParams = obj.toJSONString();
                intent.putExtra("webParams", webParams);
                startActivity(intent);
                break;
            case R.id.nav_my_coupon:
                intent = new Intent(getActivity(), MyCouponListActivity.class);
                intent.putExtra("roomId", SCCacheUtils.getCacheRoomId());
                startActivity(intent);
                break;
            case R.id.nav_my_task:
                intent = new Intent(getActivity(), TaskListActivity.class);
                intent.putExtra("roomId", SCCacheUtils.getCacheRoomId());
                startActivity(intent);
                break;
            case R.id.nav_my_message:
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //修改名称页面修改之后更新我的页面
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void updateUserInfo(ModifyUserInfo modifyUserInfo){
        if(modifyUserInfo!=null){
            if(!TextUtils.isEmpty(modifyUserInfo.getName())){
                userNikeView.setText(modifyUserInfo.getName());
            }
            if(!TextUtils.isEmpty(modifyUserInfo.getSignature())){
                tvUserSign.setText(modifyUserInfo.getSignature());
            }

        }
    }

}
