package com.shanchain.shandata.ui.view.activity.mine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.ToastUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MyStoryAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.StoryDetailInfo;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.MyStoryPresenter;
import com.shanchain.shandata.ui.presenter.impl.MyStoryPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.view.MyStoryView;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ForwardingActivity;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.other.SCEmptyView;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MyStoryActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, MyStoryView {

    @Bind(R.id.tb_my_story)
    ArthurToolBar mTbMyStory;
    @Bind(R.id.rv_my_story)
    RecyclerView mRvMyStory;
    private List<StoryDetailInfo> data = new ArrayList<>();
    private MyStoryAdapter mAdapter;
    private int page = 0;
    private int size = 10;
    private MyStoryPresenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_story;
    }

    @Override
    protected void initViewsAndEvents() {
        String reactExtra = getIntent().getStringExtra("ReactExtra");
        initToolBar();
        initData();
    }

    private void initData() {
        mPresenter = new MyStoryPresenterImpl(this);
        mPresenter.initStory(page, size);
    }

    private void initRecyclerView() {
        mRvMyStory.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyStoryAdapter(R.layout.item_story_type3, data);
        mRvMyStory.addItemDecoration(new RecyclerViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mContext, 5), getResources().getColor(R.color.colorDivider)));
        mAdapter.setEnableLoadMore(true);
        mRvMyStory.setAdapter(mAdapter);
        View emptyView = new SCEmptyView(this, R.string.str_story_empty_word, R.mipmap.abs_mylongtext_icon_longtext_default);
        mAdapter.setEmptyView(emptyView);
        mAdapter.setOnLoadMoreListener(this, mRvMyStory);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    private void initToolBar() {
        mTbMyStory.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.initStory(page, size);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        clickComment(position);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_item_story_avatar:
                clickAvatar(position);
                break;
            case R.id.iv_item_story_more:
                clickMore(position);
                break;
            case R.id.tv_item_story_forwarding:
                clickForward(position);
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
        boolean fav = mAdapter.getData().get(position).isFav();
        TextView tvLike = (TextView) mAdapter.getViewByPosition(position, R.id.tv_item_story_like);
        tvLike.setEnabled(false);
        if (fav) {   //已点赞
            mPresenter.supportCancel(mAdapter.getData().get(position).getStoryId(), position);
        } else {     //未点赞
            mPresenter.support(mAdapter.getData().get(position).getStoryId(), position);
        }
    }

    private void clickComment(int position) {
        StoryModelBean bean = getStoryModelBean(position);
        Intent intent = new Intent(mActivity, DynamicDetailsActivity.class);
        intent.putExtra("story", bean);
        startActivity(intent);
    }

    private void clickForward(int position) {
        StoryModelBean bean = getStoryModelBean(position);
        Intent intent = new Intent(mActivity, ForwardingActivity.class);
        intent.putExtra("forward", bean);
        startActivity(intent);
    }


    @NonNull
    private StoryModelBean getStoryModelBean(int position) {
        StoryDetailInfo info = mAdapter.getData().get(position);
        String characterInfo = SCCacheUtils.getCacheCharacterInfo();
        String cacheCharacterId = SCCacheUtils.getCacheCharacterId();
        String name = JSONObject.parseObject(characterInfo).getString("name");
        String headImg = JSONObject.parseObject(characterInfo).getString("headImg");
        StoryModelBean bean = new StoryModelBean();
        bean.setSupportCount(info.getSupportCount());
        bean.setCharacterId(info.getCharacterId());
        bean.setBeFav(info.isFav());
        bean.setDetailId("s" + info.getStoryId());
        bean.setCharacterImg(headImg);
        bean.setCharacterName(name);
        bean.setCommendCount(info.getCommentCount());
        bean.setCreateTime(info.getCreateTime());
        bean.setIntro(info.getIntro());
        bean.setCharacterId(Integer.parseInt(cacheCharacterId));
        bean.setSpaceId(info.getSpaceId());
        bean.setTitle(info.getTitle());
        bean.setTranspond(info.getTranspond());
        bean.setType(info.getType());
        return bean;
    }


    private void clickMore(final int position) {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        int storyId = mAdapter.getData().get(position).getStoryId();
                        int characterId = mAdapter.getData().get(position).getCharacterId();
                        reportIntent.putExtra("storyId", storyId + "");
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

    private void clickAvatar(int position) {
        ToastUtils.showToast(mContext, "自己的头像，点什么点");
    }

    private boolean isFirstLoad = true;

    @Override
    public void initStorySuc(List<StoryDetailInfo> storyDetailInfoList, boolean last) {
        if (storyDetailInfoList == null) {
            if (isFirstLoad) {
                initRecyclerView();
            } else {
                if (last) {  //没有数据了
                    mAdapter.loadMoreEnd();
                } else {     //访问出错
                    mAdapter.loadMoreFail();
                }
            }
        } else {
            if (isFirstLoad) {
                initRecyclerView();
                mAdapter.setNewData(storyDetailInfoList);
                mAdapter.disableLoadMoreIfNotFullPage(mRvMyStory);
            } else {
                mAdapter.addData(storyDetailInfoList);
            }
            mAdapter.notifyDataSetChanged();
            if (last) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
        isFirstLoad = false;
    }

    @Override
    public void supportSuc(boolean suc, int position) {
        if (suc) {
            StoryDetailInfo info = mAdapter.getData().get(position);
            int supportCount = info.getSupportCount();
            info.setFav(true);
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position, R.id.tv_item_story_like);
            tvLike.setEnabled(true);
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_selscted);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            tvLike.setText(supportCount + 1 + "");
            info.setSupportCount(supportCount + 1);
        } else {

        }
    }

    @Override
    public void supportCancelSuc(boolean suc, int position) {
        if (suc) {
            StoryDetailInfo info = mAdapter.getData().get(position);
            int supportCount = info.getSupportCount();
            info.setFav(false);
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position, R.id.tv_item_story_like);
            tvLike.setEnabled(true);
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            if (supportCount - 1 < 0) {
                tvLike.setText("0");
                info.setSupportCount(0);
            } else {
                tvLike.setText(supportCount - 1 + "");
                info.setSupportCount(supportCount - 1);
            }

        } else {

        }
    }
}
