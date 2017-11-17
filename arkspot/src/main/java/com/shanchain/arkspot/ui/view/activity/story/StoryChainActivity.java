package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.StoryChainAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.StoryInfo;
import com.shanchain.arkspot.ui.model.StoryModelInfo;
import com.shanchain.arkspot.ui.presenter.StoryChainPresenter;
import com.shanchain.arkspot.ui.presenter.impl.StoryChainPresenterImpl;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.StoryChainView;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class StoryChainActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener, StoryChainView {

    @Bind(R.id.tb_story_chain)
    ArthurToolBar mTbStoryChain;
    @Bind(R.id.rv_story_chain)
    RecyclerView mRvStoryChain;
    private List<StoryInfo> datas;

    /** 描述：是否为倒序排列*/
    private boolean isReverse = false;
    private StoryChainAdapter mStoryChainAdapter;
    private int start = 0;
    private int end = 0;
    private StoryChainPresenter mPresenter;
    private StoryModelInfo mInfo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_story_chain;
    }

    @Override
    protected void initViewsAndEvents() {
        mInfo = (StoryModelInfo) getIntent().getSerializableExtra("storyInfo");
        initToolBar();
        initData();
        initRecycler();
    }

    private void initRecycler() {
        mRvStoryChain.setLayoutManager(new LinearLayoutManager(this));

        mStoryChainAdapter = new StoryChainAdapter(R.layout.item_story_chain, datas);

        mRvStoryChain.setAdapter(mStoryChainAdapter);

        mStoryChainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StoryChainActivity.this,DynamicDetailsActivity.class);
                startActivity(intent);
            }
        });

        mStoryChainAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_item_story_avatar:
                        //头像
                        ToastUtils.showToast(StoryChainActivity.this, "头像");
                        break;
                    case R.id.tv_item_story_forwarding:
                        //转发
                        ToastUtils.showToast(StoryChainActivity.this, "转发");
                        break;
                    case R.id.tv_item_story_comment:
                        //评论
                        ToastUtils.showToast(StoryChainActivity.this, "评论");
                        break;
                    case R.id.tv_item_story_like:
                        //喜欢
                        ToastUtils.showToast(StoryChainActivity.this, "喜欢");
                        break;
                    case R.id.iv_item_story_more:
                        //举报
                        report(position);
                        break;
                }
            }
        });
    }

    private void report(final int position) {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{ R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        reportIntent.putExtra("storyId",datas.get(position).getStoryListDataBean().getStoryId()+"");
                        reportIntent.putExtra("characterId",datas.get(position).getStoryListDataBean().getInfo().getCharacterId()+"");
                        startActivity(reportIntent);
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
        mPresenter = new StoryChainPresenterImpl(this);
        String storyId = mInfo.getBean().getDetailId().substring(1);
        int genNum = mInfo.getBean().getGenNum();
        start = genNum;
        if (start>=10){
            end = genNum -10;
        }else {
            end = 0;
        }
        mPresenter.initStoryList(start,end,storyId);

        datas = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            StoryInfo info = new StoryInfo();
            info.setTime("" + i * 5);
            datas.add(info);
        }
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
        final CustomDialog customDialog = new CustomDialog(this,true,1.0,R.layout.dialog_sort
                ,new int[]{R.id.tv_sort_dialog_positive,R.id.tv_sort_dialog_reverse,R.id.tv_sort_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_sort_dialog_positive:
                        //正序
                        if (isReverse){
                            reverseDatas();
                        }
                        customDialog.dismiss();
                        break;
                    case R.id.tv_sort_dialog_reverse:
                        //倒序
                        if (!isReverse){
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
     *  描述：翻转集合数据并刷新
     */
    private void reverseDatas() {
        isReverse = !isReverse;
        Collections.reverse(datas);
        mStoryChainAdapter.notifyDataSetChanged();
    }
}
