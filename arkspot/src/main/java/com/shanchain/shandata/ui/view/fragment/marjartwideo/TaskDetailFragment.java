package com.shanchain.shandata.ui.view.fragment.marjartwideo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MultiTaskListAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.presenter.TaskDetailPresenter;
import com.shanchain.shandata.ui.presenter.impl.TaskDetailPresenterImpl;
import com.shanchain.shandata.ui.view.activity.tasklist.TaskListActivity;
import com.shanchain.shandata.ui.view.fragment.marjartwideo.view.TaskDetailListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jiguang.imui.model.ChatEventMessage;

/**
 * Created by WealChen
 * Date : 2019/7/19
 * Describe :社区帮
 */
public class TaskDetailFragment extends BaseFragment implements ArthurToolBar.OnRightClickListener,
        SwipeRefreshLayout.OnRefreshListener , TaskDetailListView {
    @Bind(R.id.tb_task_comment)
    ArthurToolBar tbTaskComment;
    @Bind(R.id.linear_add_task)
    LinearLayout linearAddTask;
    @Bind(R.id.tv_empty_word)
    TextView tvEmptyWord;
    @Bind(R.id.rv_task_details)
    RecyclerView rvTaskDetails;
    @Bind(R.id.srl_task_list)
    SwipeRefreshLayout srlTaskList;
    @Bind(R.id.tv_task_details_comment)
    TextView tvTaskDetailsComment;

    private String characterId = SCCacheUtils.getCacheCharacterId();
    private String roomId = SCCacheUtils.getCacheRoomId();
    private View footerView;
    private String roomid;
    private int pageNo = 1;
    private List<ChatEventMessage> taskList = new ArrayList();
    private MultiTaskListAdapter adapter;
    private TaskDetailPresenter mTaskDetailPresenter;
    private boolean isLast = false;//是否最后一页
    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.activity_task_detail, null);
    }

    public static TaskDetailFragment newInstance() {
        TaskDetailFragment fragment = new TaskDetailFragment();
        return fragment;
    }

    @Override
    public void initData() {
        srlTaskList.setOnRefreshListener(this);
        srlTaskList.setColorSchemeColors(getResources().getColor(R.color.login_marjar_color),
                getResources().getColor(R.color.register_marjar_color),getResources().getColor(R.color.google_yellow));
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_more, null);
        mTaskDetailPresenter = new TaskDetailPresenterImpl(this);
        initToolBar();
        initLoadMoreListener();
    }

    private void initToolBar() {
        tbTaskComment.setTitleText(getResources().getString(R.string.shequbang));
        tbTaskComment.setTitleTextColor(Color.BLACK);
        tbTaskComment.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tbTaskComment.setLeftTitleLayoutView(View.INVISIBLE);
        tbTaskComment.setRightText(getString(R.string.my_));
        tbTaskComment.setOnRightClickListener(this);

        //构造Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new MultiTaskListAdapter(getActivity(), taskList, new int[]{
                R.layout.item_task_list_type1, //所有任务，已被领取
                R.layout.item_task_list_type2,//所有任务，查看/领取任务
                R.layout.item_task_type_two,//我的任务，未领取
        });
        rvTaskDetails.setLayoutManager(linearLayoutManager);
        adapter.setHasStableIds(true);
        rvTaskDetails.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskDetailPresenter.getTaskListData(characterId,roomId,pageNo, Constants.pageSize,Constants.pullRefress);
    }

    @Override
    public void onRefresh() {
        pageNo  = 1;
        mTaskDetailPresenter.getTaskListData(characterId,roomId,pageNo, Constants.pageSize,Constants.pullRefress);
    }

    @Override
    public void onRightClick(View v) {
        Intent intent = new Intent(getActivity(), TaskListActivity.class);
        startActivity(intent);
    }



    @Override
    public void showProgressStart() {
        showLoadingDialog(false);
    }

    @Override
    public void showProgressEnd() {
        closeLoadingDialog();
    }

    @Override
    public void setTaskDetailList(String response,int pullType) {
        srlTaskList.setRefreshing(false);
        String code = JSONObject.parseObject(response).getString("code");
        if (TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
            LogUtils.d("TaskPresenterImpl", "查询任务成功");
            String data = JSONObject.parseObject(response).getString("data");
            String content = JSONObject.parseObject(data).getString("content");
            String isL = JSONObject.parseObject(response).getString("last");
            isLast  = Boolean.valueOf(isL);
            List<ChatEventMessage> chatEventMessageList = JSONObject.parseArray(content
                    , ChatEventMessage.class);
            if (chatEventMessageList.size() <= 0) {
                tvEmptyWord.setVisibility(View.VISIBLE);
            }
            if(pullType == Constants.pullRefress){
                taskList.clear();
                taskList.addAll(chatEventMessageList);
                rvTaskDetails.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else {
                adapter.addData(chatEventMessageList);
            }

        }
    }

    //上拉加载
    private void initLoadMoreListener() {
        rvTaskDetails.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(isLast){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()){
                    pageNo ++;
                    mTaskDetailPresenter.getTaskListData(characterId,roomId,pageNo,Constants.pageSize,Constants.pillLoadmore);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
