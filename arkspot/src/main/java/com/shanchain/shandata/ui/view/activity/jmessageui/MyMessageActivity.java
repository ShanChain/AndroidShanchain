package com.shanchain.shandata.ui.view.activity.jmessageui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.shanchain.shandata.R;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.List;

import butterknife.Bind;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;

public class MyMessageActivity extends BaseActivity {
    @Bind({R.id.tb_main})
    ArthurToolBar toolBar;
    @Bind(R.id.rv_message_list)
    RecyclerView rvMessageList;

    List<Conversation> conversationList;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();

    }

    private void initData() {
        conversationList =JMessageClient.getConversationList();
    }

    private void initToolBar() {
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.isShowChatRoom(false);//不在导航栏显示聊天室信息
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        toolBar.getTitleView().setLayoutParams(layoutParams);
        toolBar.setTitleText("消息");
        toolBar.setBackgroundColor(Color.parseColor("#4FD1F6"));
    }
}
