package com.shanchain.shandata.ui.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.eventbus.EventConstant;
import com.shanchain.data.common.eventbus.SCBaseEvent;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CurrentAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.event.ReleaseSucEvent;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.AttentionPresenter;
import com.shanchain.shandata.ui.presenter.impl.AttentionPresenterImpl;
import com.shanchain.shandata.ui.view.activity.story.ForwardingActivity;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.NovelDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.ui.view.fragment.view.AttentionView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by zhoujian on 2017/8/23.
 */

public class AttentionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AttentionView, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.xrv_attention)
    RecyclerView mXrvAttention;
    @Bind(R.id.srl_fragment_attention)
    SwipeRefreshLayout mSrlFragmentAttention;
    List<StoryBeanModel> datas = new ArrayList<>();
    private CurrentAdapter mAdapter;
    private int page = 0;
    private int size = 10;
    private AttentionPresenter mPresenter;
    private boolean isLoadMore;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_attention, null);
    }

    @Override
    public void initData() {
        mPresenter = new AttentionPresenterImpl(this);

        mSrlFragmentAttention.setColorSchemeColors(getResources().getColor(R.color.colorActive));
        mSrlFragmentAttention.setOnRefreshListener(this);
        mXrvAttention.setLayoutManager(new LinearLayoutManager(mActivity));
        mSrlFragmentAttention.setRefreshing(true);
        mPresenter.initData(page, size);

        mAdapter = new CurrentAdapter(datas);
        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation();
        mXrvAttention.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 5), getResources().getColor(R.color.colorDivider)));
        mXrvAttention.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mXrvAttention);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
                        StoryBeanModel beanModelTopic = mAdapter.getData().get(position);
                        intentType3.putExtra("topic", beanModelTopic);
                        startActivity(intentType3);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 描述：头像的点击事件
     */
    private void clickAvatar(int position) {
        Intent intent = new Intent(mActivity, FriendHomeActivity.class);
        int userId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
        intent.putExtra("characterId", userId);
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
            mPresenter.storyCancelSupport(position, storyId.substring(1));
        } else {     //未点赞
            mPresenter.storySupport(position, storyId.substring(1));
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
                        reportIntent.putExtra("characterId", characterId +"");
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

    /**
     * 描述：下拉刷新
     */
    @Override
    public void onRefresh() {
        isLoadMore = false;
        page = 0;
        mPresenter.refresh(page, size);
        mAdapter.loadMoreComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SCBaseEvent event){
        if(event.receiver.equalsIgnoreCase(EventConstant.EVENT_MODULE_ARKSPOT) && event.key.equalsIgnoreCase(EventConstant.EVENT_KEY_RELEASE)){
            ReleaseSucEvent releaseSucEvent = (ReleaseSucEvent) event.params;
            if (releaseSucEvent.isSuc()){
                LogUtils.i("发布成功，刷新数据");
                onRefresh();
            }
        }
    }

    @Override
    public void initSuccess(List<StoryBeanModel> list, boolean isLast) {
        if (mSrlFragmentAttention != null) {
            mSrlFragmentAttention.setRefreshing(false);
        }
        if (list == null) {
            if (isLast){
                mAdapter.loadMoreEnd();
            }else {
                mAdapter.loadMoreFail();
            }
            return;
        } else {
            if (isLoadMore) {
                if (isLast) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
                //datas.addAll(list);
                mAdapter.addData(list);
            } else {
                /*datas.clear();
                datas.addAll(list);*/
                mAdapter.setNewData(list);
                mAdapter.disableLoadMoreIfNotFullPage(mXrvAttention);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void supportCancelSuccess(boolean isSuccess, int position) {
        //ToastUtils.showToast(mActivity, isSuccess ? "取消点赞成功" : "取消点赞失败");
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
    public void supportSuccess(boolean isSuccess, int position) {
        //ToastUtils.showToast(mActivity, isSuccess ? "点赞成功" : "点赞失败");
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

    /**
     * 描述：上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        isLoadMore = true;
        page++;
        mPresenter.loadMore(page, size);
    }

}
