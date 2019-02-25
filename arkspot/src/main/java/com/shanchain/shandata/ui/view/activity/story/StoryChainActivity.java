package com.shanchain.shandata.ui.view.activity.story;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.StoryChainAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.StoryChainModel;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.presenter.StoryChainPresenter;
import com.shanchain.shandata.ui.presenter.impl.StoryChainPresenterImpl;
import com.shanchain.shandata.ui.view.activity.story.stroyView.StoryChainView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class StoryChainActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, StoryChainView, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tb_story_chain)
    ArthurToolBar mTbStoryChain;
    @Bind(R.id.rv_story_chain)
    RecyclerView mRvStoryChain;
    private List<StoryChainModel> datas = new ArrayList<>();

    /**
     * 描述：是否为倒序排列
     */
    private boolean isReverse = false;
    private StoryChainAdapter mAdapter;
    private int start = 0;
    private int end = 0;
    private StoryChainPresenter mPresenter;
    private StoryModelInfo mInfo;
    private boolean isFirstLoad = true;
    private String mStoryId;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_story_chain;
    }

    @Override
    protected void initViewsAndEvents() {
        mInfo = (StoryModelInfo) getIntent().getSerializableExtra("storyInfo");
        mStoryId = mInfo.getBean().getDetailId().substring(1);
        mPresenter = new StoryChainPresenterImpl(this);
        int genNum = mInfo.getBean().getGenNum();
        start = genNum;
        if (start >= 10) {
            end = genNum - 10;
        } else {
            end = 0;
        }
        initToolBar();
        initRecycler();
        initData();
    }

    private void initRecycler() {
        mRvStoryChain.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StoryChainAdapter(R.layout.item_story_type3, datas);
        mAdapter.setEnableLoadMore(true);
        mRvStoryChain.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mRvStoryChain);

    }

    private void report(final int position) {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        // TODO: 2017/11/23  
                      /*  StoryModelBean modelBean = mAdapter.getData().get(position);
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        reportIntent.putExtra("storyId", modelBean.getDetailId() + "");
                        reportIntent.putExtra("characterId", modelBean.getCharacterId() + "");
                        startActivity(reportIntent);*/
                        customDialog.dismiss();
                        break;
                    case R.id.tv_report_dialog_cancel:
                        //取消
                        customDialog.dismiss();
                        break;
                }
            }
        });
        customDialog.show();
    }


    private void initData() {
        mPresenter.initStoryList(start, end, mStoryId);
    }

    private void initToolBar() {
        mTbStoryChain.setOnLeftClickListener(this);
        mTbStoryChain.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        final CustomDialog customDialog = new CustomDialog(this, true, 1.0, R.layout.dialog_sort
                , new int[]{R.id.tv_sort_dialog_positive, R.id.tv_sort_dialog_reverse, R.id.tv_sort_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_sort_dialog_positive:
                        //正序
                        if (isReverse) {
                            reverseDatas();
                        }
                        customDialog.dismiss();
                        break;
                    case R.id.tv_sort_dialog_reverse:
                        //倒序
                        if (!isReverse) {
                            reverseDatas();
                        }
                        customDialog.dismiss();
                        break;
                    case R.id.tv_sort_dialog_cancel:
                        customDialog.dismiss();
                        break;
                }
            }
        });
        customDialog.show();
    }


    /**
     * 描述：翻转集合数据并刷新
     */
    private void reverseDatas() {
        isReverse = !isReverse;
        Collections.reverse(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getStoryListSuc(List<StoryChainModel> modelBeanList, boolean isLast) {
        if (modelBeanList == null){
            if (isLast){
                mAdapter.loadMoreEnd();
            }else {
                mAdapter.loadMoreFail();
            }
        }else {
            if (isFirstLoad){
                mAdapter.setNewData(modelBeanList);
                mAdapter.disableLoadMoreIfNotFullPage(mRvStoryChain);
            }else {
                mAdapter.addData(modelBeanList);
            }
            mAdapter.notifyDataSetChanged();
            if (isLast) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }

        }

    }

    @Override
    public void supportSuc(boolean suc, int position) {

    }

    @Override
    public void supportCancelSuc(boolean suc, int position) {

    }

    @Override
    public void onLoadMoreRequested() {
        initData();
    }
}
