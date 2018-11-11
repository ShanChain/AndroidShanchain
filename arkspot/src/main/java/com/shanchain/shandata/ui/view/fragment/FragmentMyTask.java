package com.shanchain.shandata.ui.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;


/**
 * Created by zhoujian on 2017/8/23.
 */

public class FragmentMyTask extends Fragment {

    @Bind(R.id.rv_task_list)
    RecyclerView rvTaskList;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        ButterKnife.bind(this, view);

        List taskList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            ChatEventMessage chatEventMessage = new ChatEventMessage("测试",IMessage.MessageType.RECEIVE_TEXT.ordinal());
            chatEventMessage.setIntro("测试");
            chatEventMessage.setTimeString("2018-12-10");
            chatEventMessage.setBounty(80);
            taskList.add(chatEventMessage);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,true);
        TaskListAdapter adapter = new TaskListAdapter(getContext(),taskList);
        rvTaskList.setLayoutManager(linearLayoutManager);
        rvTaskList.setAdapter(adapter);




        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
