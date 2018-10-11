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
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CurrentAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.ui.model.StoryBeanModel;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.presenter.RecommendPresenter;
import com.shanchain.shandata.ui.presenter.impl.RecommendPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.ui.view.activity.story.ChainActivity;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ForwardingActivity;
import com.shanchain.shandata.ui.view.activity.story.NovelDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.ui.view.fragment.view.RecommendView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by zhoujian on 2017/8/23.
 */

public class RecommendedFragment extends BaseFragment implements RecommendView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {
    @Bind(R.id.rv_fragment_recommend)
    RecyclerView mRvFragmentRecommend;
    @Bind(R.id.srl_fragment_recommend)
    SwipeRefreshLayout mSrlFragmentRecommend;
    private RecommendPresenter mPresenter;
    private List<StoryBeanModel> datas = new ArrayList<>();
    private int page = 0;
    private int size = 10;
    private CurrentAdapter mAdapter;
    private boolean isLoadMore;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_recommended, null);
    }

    @Override
    public void initData() {
        mPresenter = new RecommendPresenterImpl(this);
        mSrlFragmentRecommend.setColorSchemeColors(getResources().getColor(R.color.colorActive));
        mSrlFragmentRecommend.setOnRefreshListener(this);
        mSrlFragmentRecommend.setRefreshing(true);
        mPresenter.initData(page, size);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRvFragmentRecommend.setLayoutManager(new LinearLayoutManager(mActivity));
        mRvFragmentRecommend.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 5), getResources().getColor(R.color.colorDivider)));
        mAdapter = new CurrentAdapter(datas);
        mAdapter.setEnableLoadMore(true);
        mRvFragmentRecommend.setAdapter(mAdapter);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(this, mRvFragmentRecommend);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        page = 0;
        mPresenter.refreshData(page, size);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void initSuccess(List<StoryBeanModel> list, boolean isLast) {
        if (mSrlFragmentRecommend != null) {
            mSrlFragmentRecommend.setRefreshing(false);
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
                if (isLast) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
                mAdapter.addData(list);
            } else {
                mAdapter.setNewData(list);
                mAdapter.disableLoadMoreIfNotFullPage(mRvFragmentRecommend);
            }
            mAdapter.notifyDataSetChanged();

        }
    }

    //初始化动态删除按钮
    public void deleteDialog(final int position){
        CustomDialog sureDialog = new CustomDialog(mActivity, false, 1.0, R.layout.common_dialog_comment_delete,
                new int[]{R.id.tv_dialog_delete_cancel,R.id.tv_dialog_delete_sure,R.id.dialog_msg},"确定要删除此动态么");
        sureDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()){
                    case R.id.dialog_msg:
                        break;
                    case R.id.tv_dialog_delete_sure:

                        int deleteStoryId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getRootId();
                        mPresenter.deleteSelfStory(position,String.valueOf(deleteStoryId));

                        break;
                    case R.id.tv_dialog_delete_cancel:
                        dialog.dismiss();

                        break;
                }
            }
        });
        sureDialog.show();
    }


    @Override
    public void deleteSuccess(boolean isSuccess, int position) {
        if (isSuccess){
            mPresenter.initData(page,size);
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
            if (supportCount - 1 <= 0) {
                tvLike.setText("0");
                bean.setSupportCount(0);
            } else {
                tvLike.setText(supportCount - 1 + "");
                bean.setSupportCount(supportCount - 1);
            }
        } else {

        }
    }

    /**
     * 描述：加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        isLoadMore = true;
        page++;
        mPresenter.loadMore(page, size);
    }

    /**
     * 描述：条目按钮点击事件
     */
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
     * 描述： 展开楼层
     */
    private void expendFloors(int position) {
        StoryModelInfo modelInfo = mAdapter.getData().get(position).getStoryModel().getModelInfo();
        Intent intent = new Intent(mActivity, ChainActivity.class);
        intent.putExtra("storyInfo", modelInfo);
        startActivity(intent);
    }

    /**
     * 描述：喜欢
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

    /**
     * 描述：评论
     */
    private void clickComment(int position) {
        StoryBeanModel beanModel = mAdapter.getData().get(position);
        StoryModelBean bean = beanModel.getStoryModel().getModelInfo().getBean();
        int itemType = beanModel.getItemType();
        if (itemType == StoryInfo.type1) {   //普通动态
            Intent intent = new Intent(mActivity, DynamicDetailsActivity.class);
            intent.putExtra("story", bean);
            startActivity(intent);
        } else if (itemType == StoryInfo.type2) { //小说
            Intent intentType2 = new Intent(mActivity, NovelDetailsActivity.class);
            intentType2.putExtra("story", bean);
            startActivity(intentType2);
        }
    }

    /**
     * 描述：转发
     */
    private void clickForwarding(int position) {
        StoryModelBean bean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
        int spaceId = bean.getSpaceId();
        String cacheSpaceId = SCCacheUtils.getCacheSpaceId();
        if (!TextUtils.equals(cacheSpaceId, spaceId + "")) {
            ToastUtils.showToast(mActivity, "不同世界不能进行转发操作");
        }else {
            Intent intent = new Intent(mActivity, ForwardingActivity.class);
            intent.putExtra("forward", bean);
            startActivity(intent);
        }

    }

    /**
     * 描述：举报
     */
    private void report(final int position) {
        //推荐故事详情
        final StoryModelBean storyModelBean = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean();
        String currentCharacterId =  String.valueOf(storyModelBean.getCharacterId());//当前故事id
        String myCharacterId = SCCacheUtils.getCacheCharacterId();
        Boolean isShow = currentCharacterId.equals(myCharacterId);

        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel,R.id.tv_report_dialog_delete},isShow);
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_delete:
                        customDialog.dismiss();
                        deleteDialog(position);
                        break;
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        String storyId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getStoryId();
                        int characterId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
                        reportIntent.putExtra("storyId", storyId);
                        reportIntent.putExtra("characterId", characterId + "");
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
     * 描述：头像点击
     */
    private void clickAvatar(int position) {
        Intent intent = new Intent(mActivity, FriendHomeActivity.class);
        int characterId = mAdapter.getData().get(position).getStoryModel().getModelInfo().getBean().getCharacterId();
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    /**
     * 描述：条目点击事件
     */
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
                String topicId = data.get(position).getStoryModel().getModelInfo().getBean().getTopicModel().getTopicId()+"";
                intentType3.putExtra("topicId", topicId);
                startActivity(intentType3);
                break;
            default:
                break;
        }
    }
}
