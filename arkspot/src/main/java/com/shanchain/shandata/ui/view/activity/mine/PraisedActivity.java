package com.shanchain.shandata.ui.view.activity.mine;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MyStoryAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.StoryContentBean;
import com.shanchain.shandata.ui.model.StoryInfo;
import com.shanchain.shandata.ui.presenter.PraisedPresenter;
import com.shanchain.shandata.ui.presenter.impl.PraisedPresenterImpl;
import com.shanchain.shandata.ui.view.activity.mine.view.PraisedView;
import com.shanchain.shandata.ui.view.activity.story.ChainActivity;
import com.shanchain.shandata.ui.view.activity.story.DynamicDetailsActivity;
import com.shanchain.shandata.ui.view.activity.story.ReportActivity;
import com.shanchain.shandata.ui.view.activity.story.TopicDetailsActivity;
import com.shanchain.shandata.widgets.dialog.CustomDialog;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.other.SCEmptyView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class PraisedActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, PraisedView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tb_praised)
    ArthurToolBar mTbPraised;
    @Bind(R.id.rv_praised)
    RecyclerView mRvPraised;
    @Bind(R.id.srl_praised)
    SwipeRefreshLayout mSrlPraised;
    private List<StoryContentBean> mDatas = new ArrayList<>();
    private MyStoryAdapter mAdapter;
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
        } else if (reactExtra.equals("story")) {
            isPraised = false;
            tbTitle = getString(R.string.str_tb_title_my_stories);
            emptyView = new SCEmptyView(this, R.string.str_story_empty_word, R.mipmap.abs_mylongtext_icon_longtext_default);
        }
        initToolBar();
        initData();
        //initRecyclerView();
    }

    private void initRecyclerView() {
        mRvPraised.setLayoutManager(new LinearLayoutManager(this));
        mSrlPraised.setOnRefreshListener(this);
        mSrlPraised.setEnabled(false);
        mAdapter = new MyStoryAdapter(mDatas);
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
                int viewType = adapter.getItemViewType(position);
                switch (viewType) {
                    case StoryInfo.type1:
                        //类型1的条目点击事件
                        Intent intentType1 = new Intent(mActivity, ChainActivity.class);
                        startActivity(intentType1);
                        break;
                    case StoryInfo.type2:
                        //类型2的条目点击事件
                        Intent intentType2 = new Intent(mActivity, DynamicDetailsActivity.class);
                        startActivity(intentType2);
                        break;
                    case StoryInfo.type3:
                        //类型3的条目点击事件
                        Intent intentType3 = new Intent(mActivity, DynamicDetailsActivity.class);
                        startActivity(intentType3);
                        break;
                    case StoryInfo.type4:
                        //类型4的条目点击事件
                        Intent intentType4 = new Intent(mActivity, TopicDetailsActivity.class);
                        startActivity(intentType4);
                        break;
                }
            }
        });
    }

    private void initData() {
        mPresenter = new PraisedPresenterImpl(this);
        if (isPraised) {
            mPresenter.initPraiseData(page, size);
        } else {
            mPresenter.initStoryData(page, size);
        }


    }

    /**
     * 描述：头像的点击事件
     */
    private void clickAvatar(int position) {

    }

    /**
     * 描述：转发的点击事件
     */
    private void clickForwarding(int position) {

    }

    /**
     * 描述：评论的点击事件
     */
    private void clickComment(int position) {
        Intent intentComment = new Intent(mActivity, DynamicDetailsActivity.class);
        startActivity(intentComment);
    }

    /**
     * 描述：喜欢的点击事件
     */
    private void clickLike(int position) {

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
                        reportIntent.putExtra("storyId",storyId+"");
                        reportIntent.putExtra("characterId",characterId+"");
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
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                mSrlPraised.setRefreshing(false);
            }
        }, 3000);
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
                mAdapter.notifyDataSetChanged();
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
    public void initStorySuc(List<StoryContentBean> contentBeanList, boolean last) {
        if (contentBeanList == null) {
            if (isFirst) {
                initRecyclerView();
            } else {
                if (last) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreFail();
                }
                mAdapter.notifyDataSetChanged();
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
    public void onLoadMoreRequested() {
        page++;
        if (isPraised) {
            mPresenter.initPraiseData(page, size);
        } else {
            mPresenter.initStoryData(page, size);
        }

    }
}
