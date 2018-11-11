package com.shanchain.shandata.ui.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingleChatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.view.CircleImageView;
import cn.jpush.im.android.api.model.UserInfo;

public class SingerChatFragment extends BaseFragment {
    @Bind(R.id.bt_singer_chat_join)
    Button btnSingerChat;
    @Bind(R.id.iv_head_img)
    CircleImageView ivHeadImg;
    @Bind(R.id.tv_user_nike_name)
    TextView tvUserNikeName;
    @Bind(R.id.tv_user_sign)
    TextView tvUserSign;


    private FragmentTransaction fragmentTransaction;
    private SingerChatRoomFragment singerChatRoomFragment;
    private SingerChatFragment singerChatFragment;

    @Override
    public View initView() {

        return View.inflate(getActivity(), R.layout.fragment_singer_chat, null);

    }

    @Override
    public void initData() {
        View view = initView();
        final Bundle bundle = getActivity().getIntent().getExtras();
        DefaultUser userInfo =  bundle.getParcelable("userInfo");
        if (userInfo.getDisplayName() != null && userInfo.getAvatarFilePath() != null&&userInfo.getSignature()!=null) {
            tvUserNikeName.setText(userInfo.getDisplayName());
            tvUserSign.setText(userInfo.getSignature());
        }else {
            tvUserNikeName.setText("");
            tvUserSign.setText("");
        }


        btnSingerChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SingleChatActivity.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                getActivity().finish();
//                ToastUtils.showToast(getActivity(),"开启聊天");

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
