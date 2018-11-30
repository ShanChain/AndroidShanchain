package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shanchain.shandata.R;

import cn.jiguang.imui.model.DefaultUser;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

public class SingerChatInfoActivity extends AppCompatActivity {

    private Button btnSingerChat;
    private ImageView ivHeadImg;
    private TextView tvUserNikeName;
    private TextView tvUserSign;
    private DefaultUser userInfo;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_singer_chat);
        btnSingerChat = findViewById(R.id.bt_singer_chat_join);
        ivHeadImg = findViewById(R.id.iv_head_img);
        tvUserNikeName = findViewById(R.id.tv_user_nike_name);
        tvUserSign = findViewById(R.id.tv_user_sign);

        final Bundle bundle = getIntent().getExtras();
        if (bundle.getParcelable("userInfo") != null) {
            userInfo = bundle.getParcelable("userInfo");
            if (userInfo.getAvatar() != null) {
                RequestOptions options = new RequestOptions();
                options.placeholder(R.mipmap.aurora_headicon_default);
                Glide.with(SingerChatInfoActivity.this).load(userInfo.getAvatar()).apply(options).into(ivHeadImg);
            }
            if (userInfo.getDisplayName() != null && !TextUtils.isEmpty(userInfo.getDisplayName())) {
                tvUserNikeName.setText(userInfo.getDisplayName() + "");
            }
            if (userInfo.getSignature() != null) {
                tvUserSign.setText("" + userInfo.getSignature());
            }else {
                tvUserSign.setText("该用户很懒，没有设置签名");
            }
        } else if (userInfo == null) {
            userInfo = new DefaultUser(0, "qwer", "");
            userInfo.setHxUserId("qwer");
        }

        JMessageClient.getUserInfo(userInfo.getHxUserId()+"",new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, final UserInfo userInfo) {
                final UserInfo userInfo1 = userInfo;
                if (userInfo1 == null) {
                    return;
                }
                if (userInfo1.getNickname() != null || TextUtils.isEmpty(userInfo1.getNickname())) {
                    tvUserNikeName.setText(userInfo1.getNickname() + "");
                } else {
                    tvUserNikeName.setText(userInfo1.getUserName() + "");
                }
                if (userInfo1.getAvatarFile() != null) {
                    RequestOptions options = new RequestOptions();
                    options.placeholder(R.mipmap.aurora_headicon_default);
                    Glide.with(SingerChatInfoActivity.this).load(userInfo1.getAvatarFile().getAbsolutePath()).apply(options).into(ivHeadImg);
                }
                if (userInfo1.getSignature() != null || TextUtils.isEmpty(userInfo1.getSignature())) {
                    tvUserSign.setText("" + userInfo1.getSignature());
                } else {
                    tvUserSign.setText("该用户很懒，没有设置签名");
                }
            }
        });

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingerChatInfoActivity.this, SingleChatActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("userInfo", userInfo);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        };
        btnSingerChat.setOnClickListener(onClickListener);
    }
}
