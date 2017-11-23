package com.shanchain.shandata.ui.view.activity.story;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ChainAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.StoryChainModel;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.presenter.StoryChainPresenter;
import com.shanchain.shandata.ui.presenter.impl.StoryChainPresenterImpl;
import com.shanchain.shandata.ui.view.activity.story.stroyView.StoryChainView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ChainActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, StoryChainView {


    @Bind(R.id.tb_chain)
    ArthurToolBar mTbChain;
    @Bind(R.id.rv_chain)
    RecyclerView mRvChain;
    private List<StoryChainModel> chanDatas = new ArrayList<>();
    private ChainAdapter mAdapter;
    private StoryChainPresenter mPresenter;
    private StoryModelInfo mModelInfo;
    private String mStoryId;
    private int start = 0;
    private int end = 0;
    private int mGenNum;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_chain;
    }

    @Override
    protected void initViewsAndEvents() {
        mModelInfo = (StoryModelInfo) getIntent().getSerializableExtra("storyInfo");
        initToolBar();
        initRecyclerView();
        initData();
    }

    private void initData() {
        mStoryId = mModelInfo.getBean().getDetailId().substring(1);
        mGenNum = mModelInfo.getBean().getGenNum();
        start = mGenNum;
        if (mGenNum >= 10) {
            end = mGenNum - 10;
        } else {
            end = 0;
        }
        mPresenter = new StoryChainPresenterImpl(this);
        mPresenter.initStoryList(start, end, mStoryId);
    }

    private void initRecyclerView() {
        mRvChain.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChainAdapter(R.layout.item_story_chain, chanDatas);
        mAdapter.setEnableLoadMore(true);
        mRvChain.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mRvChain);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    private void initToolBar() {
        mTbChain.setOnLeftClickListener(this);
        mTbChain.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }

    @Override
    public void onLoadMoreRequested() {
        end = start - 10;
        mPresenter.initStoryList(start,end,mStoryId);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_item_story_avatar:
                clickHeadImg(position);
                break;
            case R.id.iv_item_story_more:
                clickMore(position);
                break;
            case R.id.tv_item_story_forwarding:
                clickForwarding(position);
                break;
            case R.id.tv_item_story_comment:
                clickComment(position);
                break;
            case R.id.tv_item_story_like:
                clickLike(position);
                break;
        }
    }

    private void clickLike(int position) {

    }

    private void clickComment(int position) {

    }

    private void clickForwarding(int position) {

    }

    private void clickMore(int position) {

    }

    private void clickHeadImg(int position) {

    }

    @Override
    public void getStoryListSuc(List<StoryChainModel> modelBeanList, boolean isLast) {
        if (modelBeanList == null) {
            if (isLast) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreFail();
            }
        } else {
            if (mGenNum == start) { //第一次加载
                mAdapter.setNewData(modelBeanList);
                mAdapter.disableLoadMoreIfNotFullPage(mRvChain);
            } else {        //加载更多
                mAdapter.addData(modelBeanList);
            }
            mAdapter.notifyDataSetChanged();
            if (isLast){
                mAdapter.loadMoreEnd();
            }else {
                mAdapter.loadMoreComplete();
            }

            start = mGenNum - modelBeanList.size();

        }
    }
}
