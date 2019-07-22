package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.rn.activity.SCWebViewActivity;
import com.shanchain.shandata.ui.view.activity.ModifyUserInfoActivity;
import com.shanchain.shandata.ui.view.activity.coupon.MyCouponListActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.FootPrintActivity;
import com.shanchain.shandata.ui.view.activity.jmessageui.MyMessageActivity;
import com.shanchain.shandata.ui.view.activity.settings.SettingsActivity;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.imui.view.CircleImageView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by WealChen
 * Date : 2019/7/19
 * Describe :我的
 */
public class MineFragment extends BaseFragment {
    @Bind(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_userdescipt)
    TextView tvUserdescipt;
    @Bind(R.id.im_edit)
    ImageView imEdit;
    @Bind(R.id.ll_may_wallet)
    LinearLayout llMayWallet;
    @Bind(R.id.ll_counp)
    LinearLayout llCounp;
    @Bind(R.id.ll_comunity)
    LinearLayout llComunity;
    @Bind(R.id.ll_message)
    LinearLayout llMessage;
    @Bind(R.id.ll_setting)
    LinearLayout llSetting;

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_mine_new, null);
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void initData() {
        initUserData();
    }

    //设置用户基本信息
    private void initUserData(){
        //设置头像
        UserInfo userInfo = JMessageClient.getMyInfo();
        if (userInfo != null && userInfo.getAvatarFile() != null) {
            Glide.with(this)
                    .load(userInfo.getAvatarFile().getAbsolutePath())
                    .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                    .error(R.drawable.aurora_headicon_default))
                    .into(ivUserHead);
        } else {
            Glide.with(this).load(SCCacheUtils.getCacheHeadImg())
                    .apply(new RequestOptions().placeholder(R.drawable.aurora_headicon_default)
                            .error(R.drawable.aurora_headicon_default)).into(ivUserHead);
        }
        if(userInfo!=null){
            tvUsername.setText(userInfo.getNickname());
            if(!TextUtils.isEmpty(userInfo.getSignature())){
                tvUserdescipt.setText(userInfo.getSignature());
            }else {
//                tvUserdescipt.setText(getResources().getString(R.string.nothing_left));
            }
        }

    }

    //编辑用户签名
    @OnClick(R.id.im_edit)
    void editUserInfo(){
        Intent intent = new Intent(getActivity(), ModifyUserInfoActivity.class);
        String nikeName = tvUsername.getText().toString();
        String userSign = tvUserdescipt.getText().toString();
        intent.putExtra("nikeName", nikeName);
        intent.putExtra("userSign", userSign);
        startActivity(intent);
    }

    //我的钱包
    @OnClick(R.id.ll_may_wallet)
    void myWallet(){
        Intent intent = new Intent(getActivity(), SCWebViewActivity.class);
        JSONObject obj = new JSONObject();
        obj.put("url", HttpApi.SEAT_WALLET);
        obj.put("title", getResources().getString(R.string.nav_my_wallet));
        String webParams = obj.toJSONString();
        intent.putExtra("webParams", webParams);
        startActivity(intent);
    }
    //我的马甲券
    @OnClick(R.id.ll_counp)
    void myCounp(){
        Intent intent = new Intent(getActivity(), MyCouponListActivity.class);
        intent.putExtra("roomId", SCCacheUtils.getCacheRoomId());
        startActivity(intent);
    }

    //我的社区帮
    @OnClick(R.id.ll_comunity)
    void myComunity(){
        Intent intent = new Intent(getActivity(), TaskListActivity.class);
        intent.putExtra("roomId", SCCacheUtils.getCacheRoomId());
        startActivity(intent);
    }

    //我的消息
    @OnClick(R.id.ll_message)
    void myMessage(){
        startActivity(new Intent(getActivity(),MyMessageActivity.class));
    }

    //设置
    @OnClick(R.id.ll_setting)
    void setting(){
        startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

}
