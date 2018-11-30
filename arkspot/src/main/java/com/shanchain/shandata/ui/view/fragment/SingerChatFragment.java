package com.shanchain.shandata.ui.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.view.activity.jmessageui.SingleChatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.model.DefaultUser;
import cn.jiguang.imui.view.CircleImageView;
import cn.jpush.im.android.api.model.UserInfo;

public class SingerChatFragment extends Fragment {
    private Button btnSingerChat;
    private CircleImageView ivHeadImg;
    private TextView tvUserNikeName;
    private TextView tvUserSign;
    private View.OnClickListener onClickListener;


    private FragmentTransaction fragmentTransaction;
    private SingerChatRoomFragment singerChatRoomFragment;
    private SingerChatFragment singerChatFragment;
    private DefaultUser userInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        final Bundle bundle = getActivity().getIntent().getExtras();
        userInfo = bundle.getParcelable("userInfo");
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SingleChatActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("userInfo", userInfo);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_singer_chat, container, false);
        btnSingerChat = rootView.findViewById(R.id.bt_singer_chat_join);
        ivHeadImg = rootView.findViewById(R.id.iv_head_img);
        tvUserNikeName = rootView.findViewById(R.id.tv_user_nike_name);
        tvUserSign = rootView.findViewById(R.id.tv_user_sign);

        if (userInfo.getDisplayName() != null && userInfo.getSignature() != null) {
            tvUserNikeName.setText(userInfo.getDisplayName());
            tvUserSign.setText(userInfo.getSignature());

        } else if (userInfo.getAvatarFilePath() != null) {
            Glide.with(getActivity()).load(userInfo.getAvatarFilePath()).into(ivHeadImg);
        } else {
            tvUserNikeName.setText("");
            tvUserSign.setText("");
        }

        btnSingerChat.setOnClickListener(onClickListener);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
