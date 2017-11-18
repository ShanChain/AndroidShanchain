package com.shanchain.shandata.ui.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.ui.view.activity.story.ForwardingActivity;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CurrentAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.presenter.CurrentPresenter;
import com.shanchain.shandata.ui.presenter.impl.CurrentPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.NovelDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.ui.view.activity.story.StoryChainActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.ui.view.fragment.view.CurrentView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.data.common.utils.DensityUtils;

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
    private int page = 0;
    private int size = 10;
    private boolean isLoadMore = false;

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
        mCurrentPresenter.initData(page, size);
        initRecyclerView();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRvStoryCurrent.setLayoutManager(layoutManager);
        mRvStoryCurrent.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 5), getResources().getColor(R.color.colorDivider)));
        mAdapter = new CurrentAdapter(datas);
        mAdapter.setEnableLoadMore(true);
        mRvStoryCurrent.setAdapter(mAdapter);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(this, mRvStoryCurrent);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void initSuccess(List<StoryBeanModel> list, boolean isLast) {
        if (mSrlStoryCurrent != null) {
            mSrlStoryCurrent.setRefreshing(false);
        }
        if (list == null) {
            if (isLast) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreFail();
            }
            return;
        } else {

            if (isLoadMore) {
                mAdapter.addData(list);
            } else {
                mAdapter.setNewData(list);
                mAdapter.disableLoadMoreIfNotFullPage(mRvStoryCurrent);
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
    public void supportSuccess(boolean isSuccess, int position) {
        if (isSuccess) {
            StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
            int supportCount = bean.getSupportCount();
            bean.setBeFav(true);
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position, R.id.tv_item_story_like);
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            tvLike.setText(supportCount + 1 + "");
            bean.setSupportCount(supportCount + 1);
        } else {

        }
    }

    @Override
    public void supportCancelSuccess(boolean isSuccess, int position) {
        if (isSuccess) {
            StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
            int supportCount = bean.getSupportCount();
            bean.setBeFav(false);
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position, R.id.tv_item_story_like);
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            tvLike.setText(supportCount - 1 + "");
            bean.setSupportCount(supportCount - 1);
        } else {

        }
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
            case R.id.tv_item_story_floors:
                expendFloors(position);
                break;
        }
    }

    /**
     *  描述： 展开楼层
     */
    private void expendFloors(int position) {
        StoryModelInfo modelInfo = mAdapter.getData().get(position).getStoryModel().getModelInfo();
        Intent intent = new Intent(mActivity, StoryChainActivity.class);
        intent.putExtra("storyInfo",modelInfo);
        startActivity(intent);
    }

    /**
     * 描述：头像的点击事件
     */
    private void clickAvatar(int position) {
        Intent intent = new Intent(mActivity, FriendHomeActivity.class);
        int characterId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    /**
     * 描述：转发的点击事件
     */
    private void clickForwarding(int position) {
        StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
        Intent intent = new Intent(mActivity, ForwardingActivity.class);
        intent.putExtra("forward",bean);
        startActivity(intent);
    }

    /**
     * 描述：评论的点击事件
     */
    private void clickComment(int position) {
        StoryBeanModel beanModel = mAdapter.getData().get(position);
        StoryModelBean bean = beanModel.getStoryModel().getModelInfo().getBean();
        int itemType = beanModel.getItemType();
        if (itemType == StoryInfo.type1){   //普通动态
            Intent intent = new Intent(mActivity, DynamicDetailsActivity.class);

            intent.putExtra("story", bean);
            startActivity(intent);
        }else if (itemType == StoryInfo.type2){ //小说
            Intent intentType2 = new Intent(mActivity, NovelDetailsActivity.class);
            intentType2.putExtra("story", bean);
            startActivity(intentType2);
        }

    }

    /**
     * 描述：喜欢的点击事件
     */
    private void clickLike(int position) {
        StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
        String storyId = bean.getDetailId();
        boolean beFav = bean.isBeFav();
        if (beFav) {     //已经点赞
            mCurrentPresenter.storyCancelSupport(position, storyId.substring(1));
        } else {     //未点赞
            mCurrentPresenter.storySupport(position, storyId.substring(1));
        }

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
                        String storyId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getStoryId();
                        int characterId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
                        reportIntent.putExtra("storyId", storyId);
                        reportIntent.putExtra("characterId", characterId+"");
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        int viewType = adapter.getItemViewType(position);
        switch (viewType) {
            case StoryInfo.type1:
                //类型1的条目点击事件 短故事
                Intent intentType1 = new Intent(mActivity, DynamicDetailsActivity.class);
                StoryBeanModel beanModel = mAdapter.getData().get(position);
                StoryModelBean bean = beanModel.getStoryModel().getModelInfo().getBean();
                intentType1.putExtra("story", bean);
                startActivity(intentType1);
                break;
            case StoryInfo.type2:
                //类型2的条目点击事件    长故事
                Intent intentType2 = new Intent(mActivity, NovelDetailsActivity.class);
                StoryBeanModel beanModel2 = mAdapter.getData().get(position);
                StoryModelBean bean2 = beanModel2.getStoryModel().getModelInfo().getBean();
                intentType2.putExtra("story", bean2);
                startActivity(intentType2);
                break;
            case StoryInfo.type3:
                //类型3的条目点击事件    话题
                Intent intentType3 = new Intent(mActivity, TopicDetailsActivity.class);
                intentType3.putExtra("from", 1);
                List<StoryBeanModel> data = mAdapter.getData();

                StoryBeanModel beanModelTopic = data.get(position);
                intentType3.putExtra("topic", beanModelTopic);
                startActivity(intentType3);
                break;
            default:
                break;
        }
    }


}
