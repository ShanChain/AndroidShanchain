package com.shanchain.shandata.ui.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MessageHomeAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.MessageHomeInfo;
import com.shanchain.shandata.ui.view.activity.chat.ChatRoomActivity;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;



/**
 * Created by zhoujian on 2017/8/23.
 */

public class NewsFragment extends BaseFragment {
    @Bind(R.id.rv_fragment_news)
    RecyclerView mRvFragmentNews;
    private List<MessageHomeInfo> sourceDatas;
    private List<MessageHomeInfo> topDatas;
    private MessageHomeAdapter mMessageHomeAdapter;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_news, null);
    }

    @Override
    public void initData() {
        initConversation();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRvFragmentNews.setLayoutManager(layoutManager);

        mRvFragmentNews.addItemDecoration(new RecyclerViewDivider(mActivity));
        mMessageHomeAdapter = new MessageHomeAdapter(R.layout.item_msg_home, sourceDatas);
        mRvFragmentNews.setAdapter(mMessageHomeAdapter);

        mMessageHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mActivity, ChatRoomActivity.class);
                MessageHomeInfo messageHomeInfo = sourceDatas.get(position);
                String toChatName = messageHomeInfo.getEMConversation().conversationId();
                boolean isGroup = messageHomeInfo.getEMConversation().isGroup();
                intent.putExtra("toChatName", toChatName);
                intent.putExtra("isGroup",isGroup);
                startActivity(intent);
            }
        });

        mMessageHomeAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showDialog(position);
                return true;
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage message){
        //有新消息到来
        LogUtils.d("会话fragment接收到新消息");
        initConversation();
        if (mMessageHomeAdapter != null){
            LogUtils.d("适配器不为null");
            mMessageHomeAdapter.notifyDataSetChanged();
        }else {
            LogUtils.d("适配器为null");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initConversation();
        if (mMessageHomeAdapter != null){
            mMessageHomeAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 描述：长按点击事件
     */
    private void showDialog(int position) {
        final AlertDialog builder = new AlertDialog.Builder(mActivity).create();
        View view = View.inflate(mActivity, R.layout.dialog_msg_home, null);
        TextView tvDelete = (TextView) view.findViewById(R.id.tv_dialog_msg_delete);
        TextView tvDetails = (TextView) view.findViewById(R.id.tv_dialog_msg_details);
        TextView tvTop = (TextView) view.findViewById(R.id.tv_dialog_msg_top);

        tvTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //置顶消息
                ToastUtils.showToast(mActivity, "置顶消息");
                builder.dismiss();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除消息
                ToastUtils.showToast(mActivity, "删除消息");
                builder.dismiss();
            }
        });

        tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //消息详情
                ToastUtils.showToast(mActivity, "消息详情");
                builder.dismiss();
            }
        });

        builder.setView(view);
        builder.show();
    }

    private List<EMConversation> mEMConversations = new ArrayList<>();

    /**
     * 描述：初始化会话列表
     */
    public void initConversation() {
        if (sourceDatas == null) {
            sourceDatas = new ArrayList<>();
        }

        if (topDatas == null) {
            topDatas = new ArrayList<>();
        }

        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        sourceDatas.clear();
        mEMConversations.clear();
        mEMConversations.addAll(allConversations.values());

        //按时间顺序排序
        Collections.sort(mEMConversations, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
            }
        });
        for (int i = 0; i < mEMConversations.size(); i++) {
            EMConversation emConversation = mEMConversations.get(i);
            MessageHomeInfo messageHomeInfo = new MessageHomeInfo();
            messageHomeInfo.setEMConversation(emConversation);
            LogUtils.d("会话列表 = " + mEMConversations.get(i).getLastMessage().getBody().toString());

            sourceDatas.add(messageHomeInfo);
        }



    }

}
