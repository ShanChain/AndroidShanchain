package com.shanchain.shandata.ui.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.SCHttpCallBack;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CurrentAdapter;
import com.shanchain.shandata.adapter.TaskAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.event.DynamicCommentEvent;
import com.shanchain.shandata.event.ReleaseSucEvent;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.presenter.CurrentPresenter;
import com.shanchain.shandata.ui.presenter.impl.CurrentPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.ui.view.activity.story.ChainActivity;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ForwardingActivity;
import com.shanchain.shandata.ui.view.activity.story.NovelDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.ui.view.fragment.view.CurrentView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.jiguang.imui.model.ChatEventMessage;
import okhttp3.Call;

public class FragmentTaskList extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.rv_story_current)
    RecyclerView mRvStoryCurrent;
    @Bind(R.id.srl_story_current)
    SwipeRefreshLayout mSrlStoryCurrent;
    private CurrentPresenter mCurrentPresenter;
    List<ChatEventMessage> datas = new ArrayList<>();
    private TaskAdapter mAdapter;
    private int page = 0;
    private int size = 10;
    private boolean isLoadMore = false;
    private StoryBeanModel beanModel;
    private StoryModelBean bean;
    private  int commentCount;
    private String dialogMsgs = "确定删除此动态么";

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_current, null);
    }

    @Override
    public void initData() {
        String characterId = SCCacheUtils.getCacheCharacterId();
        String squareId = SCCacheUtils.getCacheSpaceId();
        SCHttpUtils.postWithUserId()
                .url(HttpApi.USER_TASK_LIST)
                .addParams("characterId",characterId)
                .addParams("squareId",squareId)
                .build()
                .execute(new SCHttpCallBack<ChatEventMessage>(ChatEventMessage.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(getActivity(),"获取任务列表失败");
                    }

                    @Override
                    public void onResponse(ChatEventMessage response, int id) {
//                        ToastUtils.showToast(getActivity(),"获取任务列表失败");
                        ToastUtils.showToast(getActivity(),"获取任务列表失败");
                        response.getCharacterId();

                    }

                });

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvStoryCurrent.setLayoutManager(layoutManager);
        mRvStoryCurrent.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(getActivity(), 5), getResources().getColor(R.color.colorDivider)));

        mAdapter = new TaskAdapter(datas);
        mAdapter.setEnableLoadMore(true);
        mRvStoryCurrent.setAdapter(mAdapter);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(this, mRvStoryCurrent);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onRefresh() {
        isLoadMore = false;
        page = 0;
        mCurrentPresenter.refreshData(page, size);
        mAdapter.loadMoreComplete();
    }


    @Override
    public void onLoadMoreRequested() {
        isLoadMore = true;
        page++;
        mCurrentPresenter.loadMore(page, size);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()){
            case R.id.btn_event_task:
                ToastUtils.showToast(getActivity(),"点击了领取任务按钮");
                break;
        }

    }



    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

}
