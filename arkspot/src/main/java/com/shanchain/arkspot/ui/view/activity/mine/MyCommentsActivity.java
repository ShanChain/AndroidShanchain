package com.shanchain.arkspot.ui.view.activity.mine;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.MyCommentsAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.BdMyCommentBean;
import com.shanchain.arkspot.ui.model.CommentBean;
import com.shanchain.arkspot.ui.model.CommentListMineInfo;
import com.shanchain.arkspot.ui.model.CommentMineData;
import com.shanchain.arkspot.ui.model.CommentStoryInfo;
import com.shanchain.arkspot.ui.model.ResponseStoryList;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.arkspot.widgets.other.SCEmptyView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.net.HttpApi;
import com.shanchain.data.common.net.NetErrCode;
import com.shanchain.data.common.net.SCHttpUtils;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;


public class MyCommentsActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @Bind(R.id.tb_my_comments)
    ArthurToolBar mTbMyComments;
    @Bind(R.id.rv_comments)
    RecyclerView mRvComments;
    @Bind(R.id.srl_my_comments)
    SwipeRefreshLayout mSrlMyComments;
    private List<BdMyCommentBean> datas;
    private List<BdMyCommentBean> tempDatas;
    private MyCommentsAdapter mAdapter;
    private boolean mLast;
    private int page = 0;
    private int pageSise = 10;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_comments;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData(false, true);
        //initRecyclerView();

    }

    private void initData(final boolean isLoadMore, final boolean isFirst) {
        if (isFirst){
            datas = new ArrayList<>();
            tempDatas = new ArrayList<>();
        }
        SCHttpUtils.postWithUserId()
                .url(HttpApi.COMMENT_LIST_MINE)
                .addParams("page", page + "")
                .addParams("size", pageSise + "")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        initError();
                        LogUtils.i("获取我的评论列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (TextUtils.isEmpty(response)) {
                            initError();
                            return;
                        }

                        CommentListMineInfo commentListMineInfo = JSONObject.parseObject(response, CommentListMineInfo.class);
                        if (commentListMineInfo == null) {
                            initError();
                            return;
                        }

                        String code = commentListMineInfo.getCode();
                        if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            initError();
                            return;
                        }

                        CommentMineData data = commentListMineInfo.getData();
                        if (data == null) {
                            initError();
                            return;
                        }

                        List<CommentBean> content = data.getContent();
                        mLast = data.isLast();

                        tempDatas.clear();
                        for (int i = 0; i < content.size(); i++) {
                            int storyId = content.get(i).getStoryId();
                            BdMyCommentBean bdMyCommentBean = new BdMyCommentBean();
                            bdMyCommentBean.setCommentBean(content.get(i));
                            bdMyCommentBean.setStoryId(storyId);
                            tempDatas.add(bdMyCommentBean);
                        }

                        obtainStoryInfo(tempDatas, isLoadMore, isFirst);

                    }
                });

    }

    private void obtainStoryInfo(final List<BdMyCommentBean> tempDatas, final boolean isloadMore, final boolean isFirst) {
        final List<Integer> storyIds = new ArrayList<>();
        for (int i = 0; i < tempDatas.size(); i++) {
            storyIds.add(tempDatas.get(i).getStoryId());
        }

        String dataArr = JSON.toJSONString(storyIds);

        SCHttpUtils.post()
                .url(HttpApi.STORY_GET)
                .addParams("dataArray", dataArr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        initError();
                        LogUtils.i("获取故事列表失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.i("获取故事列表成功 = " + response);
                        if (TextUtils.isEmpty(response)) {
                            return;
                        }

                        ResponseStoryList storyList = JSONObject.parseObject(response, ResponseStoryList.class);
                        if (storyList == null) {
                            initError();
                            return;
                        }

                        String code = storyList.getCode();
                        if (!TextUtils.equals(code, NetErrCode.COMMON_SUC_CODE)) {
                            initError();
                            return;
                        }
                        List<CommentStoryInfo> infoList = storyList.getData();
                        if (infoList == null) {
                            initError();
                            return;
                        }

                        for (int i = 0; i < tempDatas.size(); i++) {
                            int storyId = tempDatas.get(i).getStoryId();
                            for (CommentStoryInfo info : infoList) {
                                if (storyId == info.getStoryId()) {
                                    tempDatas.get(i).setStoryInfo(info);
                                }
                            }
                        }
                        datas.addAll(tempDatas);
                        if (isFirst) {
                            initRecyclerView();
                        } else {
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        if (isloadMore) {
                            mAdapter.loadMoreComplete();
                        }
                        if (mLast) {
                            //没有数据了
                            mAdapter.loadMoreEnd();
                        } else {
                            //不是最后一页，有数据
                            page++;
                        }
                        if (mSrlMyComments != null) {
                            mSrlMyComments.setRefreshing(false);
                        }
                    }
                });

    }

    private void initError() {
        if (mSrlMyComments!=null){
            mSrlMyComments.setRefreshing(false);
        }

        if (mAdapter != null) {
            mAdapter.loadMoreFail();
        }

    }

    private void initRecyclerView() {
        mSrlMyComments.setOnRefreshListener(this);
        View emptyView = new SCEmptyView(this, R.string.str_empty_my_comments, R.mipmap.abs_comment_icon_thumbsup_default_1);
        mRvComments.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyCommentsAdapter(R.layout.item_my_comments, datas);
        mAdapter.setEnableLoadMore(true);
        mRvComments.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 1), getResources().getColor(R.color.colorDivider)));
        mAdapter.setOnLoadMoreListener(this, mRvComments);
        mRvComments.setAdapter(mAdapter);
        mAdapter.setEmptyView(emptyView);
    }

    private void initToolBar() {
        mTbMyComments.setBtnEnabled(true, false);
        mTbMyComments.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRefresh() {
        mSrlMyComments.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSrlMyComments != null) {
                    mSrlMyComments.setRefreshing(false);
                }
            }
        }, 1000);
    }

    @Override
    public void onLoadMoreRequested() {
        initData(true,false);
    }
}
