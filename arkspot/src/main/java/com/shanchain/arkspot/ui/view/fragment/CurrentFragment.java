package com.shanchain.arkspot.ui.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.CurrentAdapter;
import com.shanchain.arkspot.base.BaseFragment;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.presenter.CurrentPresenter;
import com.shanchain.arkspot.ui.presenter.impl.CurrentPresenterImpl;
import com.shanchain.arkspot.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.arkspot.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.arkspot.ui.view.activity.story.ReportActivity;
import com.shanchain.arkspot.ui.view.fragment.view.CurrentView;
import com.shanchain.arkspot.widgets.dialog.CustomDialog;
import com.shanchain.arkspot.widgets.other.RecyclerViewDivider;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class CurrentFragment extends BaseFragment implements CurrentView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.rv_story_current)
    RecyclerView mRvStoryCurrent;
    @Bind(R.id.srl_story_current)
    SwipeRefreshLayout mSrlStoryCurrent;
    private CurrentPresenter mCurrentPresenter;
    List<StoryBeanModel> datas = new ArrayList<>();
    private CurrentAdapter mAdapter;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_current, null);
    }

    @Override
    public void initData() {
        mSrlStoryCurrent.setOnRefreshListener(this);
        mSrlStoryCurrent.setColorSchemeColors(getResources().getColor(R.color.colorActive));
        mSrlStoryCurrent.setRefreshing(true);
        mCurrentPresenter = new CurrentPresenterImpl(this);
        mCurrentPresenter.initData(0, 100);

        initRecyclerView();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRvStoryCurrent.setLayoutManager(layoutManager);

        mRvStoryCurrent.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 5), getResources().getColor(R.color.colorDivider)));
        mAdapter = new CurrentAdapter(datas);
        mAdapter.setEnableLoadMore(true);
        mRvStoryCurrent.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this,mRvStoryCurrent);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    public void initSuccess(List<StoryBeanModel> list) {
        mSrlStoryCurrent.setRefreshing(false);
        if (list == null) {

            return;
        } else {

            //测试阶段
            datas.clear();
            datas.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void supportSuccess(boolean isSuccess) {
        ToastUtils.showToast(mActivity, isSuccess ? "点赞成功" : "点赞失败");
    }

    @Override
    public void onRefresh() {
        mCurrentPresenter.refreshData(0, 100);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_item_story_avatar:
                clickAvatar(position);
                break;
            case R.id.iv_item_story_more:
                report(position);
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

    /**
     * 描述：头像的点击事件
     */
    private void clickAvatar(int position) {
        ToastUtils.showToast(mActivity, "头像");
        Intent intent = new Intent(mActivity, FriendHomeActivity.class);
        int userId = datas.get(position).getStoryModel().getModelInfo().getCharacterBrief().getCharacterId();
        intent.putExtra("characterId",userId);
        startActivity(intent);
    }

    /**
     * 描述：转发的点击事件
     */
    private void clickForwarding(int position) {
        ToastUtils.showToast(mActivity, "转发");


    }

    /**
     * 描述：评论的点击事件
     */
    private void clickComment(int position) {
        Intent intent = new Intent(mActivity, DynamicDetailsActivity.class);
        StoryBeanModel beanModel = datas.get(position);
        intent.putExtra("story",beanModel);
        startActivity(intent);
    }

    /**
     * 描述：喜欢的点击事件
     */
    private void clickLike(int position) {
        String storyId = datas.get(position).getStoryModel().getModelInfo().getBean().getDetailId();
        mCurrentPresenter.storySupport(storyId.substring(1));
    }

    private void report(final int position) {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{R.id.tv_report_dialog_shielding, R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_shielding:
                        //屏蔽
                        showShieldingDialog();
                        customDialog.dismiss();
                        break;
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        String storyId = datas.get(position).getStoryModel().getModelInfo().getStoryId();
                        int characterId = datas.get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
                        reportIntent.putExtra("storyId",storyId);
                        reportIntent.putExtra("characterId",characterId);
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

    private void showShieldingDialog() {
        final CustomDialog shieldingDialog = new CustomDialog(mActivity, false, 1, R.layout.dialog_shielding, new int[]{R.id.tv_shielding_dialog_cancel, R.id.tv_shielding_dialog_sure});
        shieldingDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_shielding_dialog_cancel:
                        shieldingDialog.dismiss();
                        break;
                    case R.id.tv_shielding_dialog_sure:
                        //确定屏蔽，请求接口

                        shieldingDialog.dismiss();
                        break;
                }
            }
        });
        shieldingDialog.show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(mActivity, DynamicDetailsActivity.class);
        StoryBeanModel beanModel = datas.get(position);
        intent.putExtra("story",beanModel);
        startActivity(intent);
    }

    @Override
    public void onLoadMoreRequested() {

    }
}
