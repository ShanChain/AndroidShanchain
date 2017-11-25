package com.shanchain.shandata.ui.view.activity.mine;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.PraisedAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.ContactBean;
import com.shanchain.shandata.ui.model.StoryContentBean;
import com.shanchain.shandata.ui.model.StoryModelBean;
import com.shanchain.shandata.ui.presenter.PraisedPresenter;
import com.shanchain.shandata.ui.presenter.impl.PraisedPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.view.PraisedView;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ForwardingActivity;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.other.SCEmptyView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class PraisedActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, PraisedView, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tb_praised)
    ArthurToolBar mTbPraised;
    @Bind(R.id.rv_praised)
    RecyclerView mRvPraised;
    private List<StoryContentBean> mDatas = new ArrayList<>();
    private PraisedAdapter mAdapter;
    private String tbTitle = "";
    private View emptyView;
    private boolean isPraised;
    private int page = 0;
    private int size = 0;
    private boolean isFirst = true;
    private PraisedPresenter mPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_praised;
    }

    @Override
    protected void initViewsAndEvents() {
        Intent intent = getIntent();
        String reactExtra = intent.getStringExtra("ReactExtra");
        if (reactExtra.equals("praised")) {
            isPraised = true;
            tbTitle = getString(R.string.str_tb_title_praised);
            emptyView = new SCEmptyView(this, R.string.str_praised_empty_word, R.mipmap.abs_liked_icon_thumbsup_default);
        }
        initToolBar();
        initData();
        //initRecyclerView();
    }

    private void initRecyclerView() {
        mRvPraised.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PraisedAdapter(mDatas);
        mAdapter.loadMoreEnd(true);
        mRvPraised.addItemDecoration(new RecyclerViewDivider(mActivity, LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(mActivity, 5), getResources().getColor(R.color.colorDivider)));
        mRvPraised.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mRvPraised);
        mAdapter.setEmptyView(emptyView);
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
                clickComment(position);
            }
        });
    }

    private void initData() {
        mPresenter = new PraisedPresenterImpl(this);
        mPresenter.initPraiseData(page, size);
    }

    /**
     * 描述：头像的点击事件
     */
    private void clickAvatar(int position) {
        ContactBean characterBean = mAdapter.getData().get(position).getContactBean();
        int characterId = characterBean.getCharacterId();
        Intent intent = new Intent(this, FriendHomeActivity.class);
        intent.putExtra("characterId", characterId);
        startActivity(intent);
    }

    /**
     * 描述：转发的点击事件
     */
    private void clickForwarding(int position) {
        StoryModelBean bean = getStoryModelBean(position);
        Intent intent = new Intent(mActivity, ForwardingActivity.class);
        intent.putExtra("forward", bean);
        startActivity(intent);
    }

    @NonNull
    private StoryModelBean getStoryModelBean(int position) {
        StoryContentBean info = mAdapter.getData().get(position);
        StoryModelBean bean = new StoryModelBean();
        bean.setSupportCount(info.getSupportCount());
        bean.setCharacterId(info.getCharacterId());
        bean.setBeFav(info.isFav());
        bean.setDetailId("s" + info.getStoryId());
        bean.setCharacterImg(info.getContactBean().getHeadImg());
        bean.setCharacterName(info.getContactBean().getName());
        bean.setCommendCount(info.getCommentCount());
        bean.setCreateTime(info.getCreateTime());
        bean.setIntro(info.getIntro());
        bean.setCharacterId(info.getContactBean().getCharacterId());
        bean.setSpaceId(info.getSpaceId());
        bean.setTitle(info.getTitle());
        bean.setTranspond(info.getTranspond());
        bean.setType(info.getType());
        return bean;
    }


    /**
     * 描述：评论的点击事件
     */
    private void clickComment(int position) {
        StoryModelBean bean = getStoryModelBean(position);
        Intent intent = new Intent(mActivity, DynamicDetailsActivity.class);
        intent.putExtra("story", bean);
        startActivity(intent);
    }

    /**
     * 描述：喜欢的点击事件
     */
    private void clickLike(int position) {
        StoryContentBean storyContentBean = mAdapter.getData().get(position);
        boolean fav = storyContentBean.isFav();
        if (fav){
            mPresenter.supportCancel(storyContentBean.getStoryId(),position);
        }else {
            mPresenter.support(storyContentBean.getStoryId(),position);
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


    private void initToolBar() {
        mTbPraised.setTitleText(tbTitle);
        mTbPraised.setBtnEnabled(true, false);
        mTbPraised.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void initPraisedSuc(List<StoryContentBean> contentBeanList, boolean last) {
        if (contentBeanList == null) {
            if (isFirst) {
                initRecyclerView();
            } else {
                if (last) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreFail();
                }
            }
        } else {
            if (isFirst) {
                initRecyclerView();
                mAdapter.setNewData(contentBeanList);
                mAdapter.disableLoadMoreIfNotFullPage(mRvPraised);
            } else {
                mAdapter.addData(contentBeanList);
            }
            mAdapter.notifyDataSetChanged();
            if (last) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        }
        isFirst = false;
    }

    @Override
    public void supportSuc(boolean suc, int position) {
        if (suc) {
            StoryContentBean bean = mAdapter.getData().get(position);
            int supportCount = bean.getSupportCount();
            bean.setFav(true);
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position, R.id.tv_item_story_like);
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
            StoryContentBean bean = mAdapter.getData().get(position);
            int supportCount = bean.getSupportCount();
            bean.setFav(false);
            TextView tvLike = (TextView) mAdapter.getViewByPosition(position, R.id.tv_item_story_like);
            tvLike.setEnabled(true);
            Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.abs_home_btn_thumbsup_default);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
            tvLike.setCompoundDrawablePadding(DensityUtils.dip2px(mActivity, 10));
            if (supportCount - 1 < 0) {
                tvLike.setText("0");
                bean.setSupportCount(0);
            } else {
                tvLike.setText(supportCount - 1 + "");
                bean.setSupportCount(supportCount - 1);
            }

        } else {

        }
    }


    @Override
    public void onLoadMoreRequested() {
        page++;
        mPresenter.initPraiseData(page, size);

    }
}
