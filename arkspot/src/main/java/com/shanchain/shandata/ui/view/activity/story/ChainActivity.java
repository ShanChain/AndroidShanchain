package com.shanchain.shandata.ui.view.activity.story;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
import com.shanchain.shandata.adapter.ChainAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.StoryChainBean;
import com.shanchain.shandata.ui.model.StoryChainModel;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.model.StoryModelInfo;
import com.shanchain.shandata.ui.presenter.StoryChainPresenter;
import com.shanchain.shandata.ui.presenter.impl.StoryChainPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.FriendHomeActivity;
import com.shanchain.shandata.ui.view.activity.story.stroyView.StoryChainView;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.data.common.ui.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.Collections;
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
    private boolean isReverse = false;

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
        end = start - 10;
        mPresenter = new StoryChainPresenterImpl(this);
        mPresenter.initStoryList(start, end, mStoryId);
    }

    private void initRecyclerView() {
        mRvChain.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChainAdapter(R.layout.item_story_chain, chanDatas);
        mAdapter.setEnableLoadMore(false);
        mRvChain.setAdapter(mAdapter);
        //mAdapter.setOnLoadMoreListener(this, mRvChain);
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
        final CustomDialog customDialog = new CustomDialog(this, true, 1.0, R.layout.dialog_sort
                , new int[]{R.id.tv_sort_dialog_positive, R.id.tv_sort_dialog_reverse, R.id.tv_sort_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_sort_dialog_positive:
                        //正序
                        if (isReverse) {
                            reverseData();
                        }
                        customDialog.dismiss();
                        break;
                    case R.id.tv_sort_dialog_reverse:
                        //倒序
                        if (!isReverse) {
                            reverseData();
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

    private void reverseData() {
        Collections.reverse(mAdapter.getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreRequested() {
//        end = start - 10;
//        mPresenter.initStoryList(start, end, mStoryId);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        clickComment(position);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_item_story_avatar:
                clickHeadImg(position);
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

    private void clickLike(int position) {
        TextView tvLike = (TextView) mAdapter.getViewByPosition(mRvChain,position,R.id.tv_item_story_like);
        tvLike.setEnabled(false);
        boolean beFav = mAdapter.getData().get(position).isBeFav();
        int storyId = mAdapter.getData().get(position).getStoryId();
        if (beFav){
            mPresenter.supportCancel(storyId,position);
        }else {
            mPresenter.support(storyId,position);
        }

    }

    private void clickComment(int position) {
        StoryModelBean bean = getStoryModelBean(position);
        Intent intent = new Intent(mActivity, DynamicDetailsActivity.class);
        intent.putExtra("story", bean);
        startActivity(intent);
    }

    @NonNull
    private StoryModelBean getStoryModelBean(int position) {
        StoryChainBean storyBean = mAdapter.getData().get(position).getStoryBean();
        ContactBean characterBean = mAdapter.getData().get(position).getCharacterBean();
        StoryModelBean bean = new StoryModelBean();
        bean.setSupportCount(storyBean.getSupportCount());
        bean.setCharacterId(storyBean.getCharacterId());
        bean.setBeFav(mAdapter.getData().get(position).isBeFav());
        bean.setDetailId("s" + storyBean.getStoryId());
        bean.setCharacterImg(characterBean.getHeadImg());
        bean.setCharacterName(characterBean.getName());
        bean.setCommendCount(storyBean.getCommentCount());
        bean.setCreateTime(storyBean.getCreateTime());
        bean.setIntro(storyBean.getIntro());
        bean.setCharacterId(characterBean.getCharacterId());
        bean.setSpaceId(storyBean.getSpaceId());
        bean.setTitle(storyBean.getTitle());
        bean.setTranspond(storyBean.getTranspond());
        bean.setType(storyBean.getType());
        return bean;
    }

    private void clickForwarding(int position) {
        int spaceId = mAdapter.getData().get(position).getStoryBean().getSpaceId();
        String cacheSpaceId = SCCacheUtils.getCacheSpaceId();
        if (TextUtils.equals(spaceId+"",cacheSpaceId)){
            StoryModelBean bean = getStoryModelBean(position);
            Intent intent = new Intent(mActivity, ForwardingActivity.class);
            intent.putExtra("forward", bean);
            startActivity(intent);
        }else {
            ToastUtils.showToast(mContext,"不同世界不能进行转发操作");
        }

    }


    private void clickHeadImg(int position) {
        ContactBean characterBean = mAdapter.getData().get(position).getCharacterBean();
        int characterId = characterBean.getCharacterId();
        Intent intent = new Intent(this, FriendHomeActivity.class);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    @Override
    public void getStoryListSuc(List<StoryChainModel> modelBeanList, boolean isLast) {
        if (modelBeanList == null) {

        } else {
            mAdapter.setNewData(modelBeanList);
            mAdapter.notifyDataSetChanged();
            mAdapter.disableLoadMoreIfNotFullPage(mRvChain);
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void supportSuc(boolean suc, int position) {
        if (suc) {
            StoryChainBean bean = mAdapter.getData().get(position).getStoryBean();
            StoryChainModel model = mAdapter.getData().get(position);
            int supportCount = bean.getSupportCount();
            model.setBeFav(true);
            TextView tvLike = (TextView) mAdapter.getViewByPosition(mRvChain,position, R.id.tv_item_story_like);
            tvLike.setEnabled(true);
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
    public void supportCancelSuc(boolean suc, int position) {
        if (suc) {
            StoryChainBean bean = mAdapter.getData().get(position).getStoryBean();
            StoryChainModel model = mAdapter.getData().get(position);
            int supportCount = bean.getSupportCount();
            model.setBeFav(false);
            TextView tvLike = (TextView) mAdapter.getViewByPosition(mRvChain,position, R.id.tv_item_story_like);
            tvLike.setEnabled(true);
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

    private void report(final int position) {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, 1.0, R.layout.dialog_shielding_report,
                new int[]{R.id.tv_report_dialog_report, R.id.tv_report_dialog_cancel});
        customDialog.setOnItemClickListener(new CustomDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.tv_report_dialog_report:
                        //举报
                        Intent reportIntent = new Intent(mActivity, ReportActivity.class);
                        String storyId = mAdapter.getData().get(position).getStoryBean().getStoryId() + "";
                        int characterId = mAdapter.getData().get(position).getCharacterBean().getCharacterId();
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
}
