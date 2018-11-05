package com.shanchain.shandata.ui.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

public class FragmentMyTask extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

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
        return View.inflate(mTaskListActivity, R.layout.fragment_current, null);
    }

    @Override
    public void initData() {
        mSrlStoryCurrent.setOnRefreshListener(this);
        mSrlStoryCurrent.setColorSchemeColors(getResources().getColor(R.color.colorActive));
        mSrlStoryCurrent.setRefreshing(true);
//        mCurrentPresenter = new CurrentPresenterImpl(this);
//        mCurrentPresenter.initData(page, size);
        initRecyclerView();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mTaskListActivity);
        mRvStoryCurrent.setLayoutManager(layoutManager);
        mRvStoryCurrent.addItemDecoration(new RecyclerViewDivider(mTaskListActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mTaskListActivity, 5), getResources().getColor(R.color.colorDivider)));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SCBaseEvent event) {
        if (event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_RELEASE)) {
            ReleaseSucEvent releaseSucEvent = (ReleaseSucEvent) event.params;
            if (releaseSucEvent.isSuc()) {
                LogUtils.i("发布成功，刷新数据");
                onRefresh();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEvent(DynamicCommentEvent event) {
        if (mAdapter.getData() != null && mAdapter.getData().size() >= 0) {
            event.setBean(bean);
            onRefresh();
            //设置刷新后的评论总数
            if (event.isAdd()) {
                commentCount++;
                event.setCommentCount(commentCount);
            } else {
                commentCount--;
                event.setCommentCount(commentCount);

            }
        }


    }


    @Override
    public void onLoadMoreRequested() {
        isLoadMore = true;
        page++;
        mCurrentPresenter.loadMore(page, size);
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_item_story_avatar:

                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        int viewType = adapter.getItemViewType(position);
        switch (viewType) {
            case StoryInfo.type1:
                //类型1的条目点击事件 短故事
                break;
            default:
                break;
        }
    }

}

