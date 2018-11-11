package com.shanchain.shandata.ui.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TaskListAdapter;
import com.shanchain.shandata.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.model.ChatEventMessage;


public class FragmentTaskList extends BaseFragment {


    @Bind(R.id.rv_task_list)
    RecyclerView rvTaskList;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;

    @Override
    public View initView() {
        return View.inflate(getContext(), R.layout.fragment_task_list, null);
    }

    @Override
    public void initData() {

        String roomId = "12766207";
        initRecyclerView();
    }

    private void initRecyclerView() {
        List taskList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            ChatEventMessage chatEventMessage = new ChatEventMessage("测试", IMessage.MessageType.RECEIVE_TEXT.ordinal());
            chatEventMessage.setIntro("测试");
            chatEventMessage.setTimeString("2018-12-10");
            chatEventMessage.setBounty(80);
            taskList.add(chatEventMessage);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, true);
        TaskListAdapter adapter = new TaskListAdapter(getContext(), taskList);
        rvTaskList.setLayoutManager(linearLayoutManager);
        rvTaskList.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
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
