package com.shanchain.shandata.ui.view.activity.story;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.AddRoleAdapter;
import com.shanchain.shandata.adapter.StoryTitleLikeAdapter;
import com.shanchain.shandata.adapter.StoryTitleStagAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.ui.model.SpaceInfo;
import com.shanchain.shandata.ui.model.StoryTagInfo;
import com.shanchain.shandata.ui.model.TagContentBean;
import com.shanchain.shandata.ui.presenter.StoryTitlePresenter;
import com.shanchain.shandata.ui.presenter.impl.StoryTitlePresenterImpl;
import com.shanchain.shandata.ui.view.activity.story.stroyView.StoryTitleView;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.base.Constants;
import com.shanchain.data.common.cache.SCCacheUtils;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class StoryTitleActivity extends BaseActivity implements ArthurToolBar.OnTitleClickListener, ArthurToolBar.OnRightClickListener, StoryTitleView, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.et_story_title_search)
    EditText mEtStoryTitleSearch;
    @Bind(R.id.rv_story_title)
    RecyclerView mRvStoryTitle;
    @Bind(R.id.tb_story_title)
    ArthurToolBar mTbStoryTitle;
    private boolean spaceSuc;
    private boolean likeSuc;

    private RecyclerView mRvLike;
    private RecyclerView mRvTag;
    private List<StoryTagInfo> mTagDatas = new ArrayList<>();
    private List<SpaceInfo> mStagDatas = new ArrayList<>();
    private List<SpaceInfo> likeDatas = new ArrayList<>();
    private View mHeadView;
    private StoryTitleStagAdapter mStagAdapter;
    private AddRoleAdapter mAddRoleAdapter;
    private StoryTitleLikeAdapter mStoryTitleLikeAdapter;
    private StoryTitlePresenter mPresenter;
    private LinearLayout mLlFavorite;
    private SwipeRefreshLayout mSrlStoryTitle;
    private int page = 0;
    private int size = 15;
    private boolean isLoadMore;
    private boolean firstLoadLike = true;
    private int likePage = 0;
    private int likeSize = 10;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_story_title;
    }

    @Override
    protected void initViewsAndEvents() {
        mPresenter = new StoryTitlePresenterImpl(this);
        mSrlStoryTitle = (SwipeRefreshLayout) findViewById(R.id.srl_story_title);
        mSrlStoryTitle.setColorSchemeColors(getResources().getColor(R.color.colorActive));
        mSrlStoryTitle.setOnRefreshListener(this);
        initToolBar();
        initRecyclerView();
        initData();
    }

    private void initData() {
        mPresenter.initFavData(likePage, likeSize);
        mPresenter.initSpace(page, size);
    }

    private void initRecyclerView() {
        initHeadView();
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //防止位置发生改变
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRvStoryTitle.setLayoutManager(manager);
        mStagAdapter = new StoryTitleStagAdapter(R.layout.item_head_stagger, mStagDatas);
        mStagAdapter.addHeaderView(mHeadView);
        mStagAdapter.setEnableLoadMore(true);
        mRvStoryTitle.setAdapter(mStagAdapter);
        mStagAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRvStoryTitle);
        mStagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StoryTitleActivity.this, ChooseRoleActivity.class);
                SpaceInfo spaceInfo = mStagAdapter.getData().get(position);
                intent.putExtra("spaceInfo", spaceInfo);
                startActivity(intent);
            }
        });

        mRvStoryTitle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //防止第一行顶部有空白
                manager.invalidateSpanAssignments();

            }
        });
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this).inflate(R.layout.head_story_title, (ViewGroup) findViewById(android.R.id.content), false);
        mRvLike = (RecyclerView) mHeadView.findViewById(R.id.rv_story_title_head);
        mRvTag = (RecyclerView) mHeadView.findViewById(R.id.rv_story_title_tag);
        mLlFavorite = (LinearLayout) mHeadView.findViewById(R.id.ll_head_favorite);
        final LinearLayoutManager likeLayoutManager = new LinearLayoutManager(this);
        likeLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvLike.setLayoutManager(likeLayoutManager);

        mStoryTitleLikeAdapter = new StoryTitleLikeAdapter(R.layout.item_head_like, likeDatas);
        mRvLike.setAdapter(mStoryTitleLikeAdapter);
        mStoryTitleLikeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SpaceInfo spaceInfo = likeDatas.get(position);
                Intent intent = new Intent(mContext, ChooseRoleActivity.class);
                intent.putExtra("spaceInfo", spaceInfo);
                startActivity(intent);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRvTag.setLayoutManager(gridLayoutManager);

        mAddRoleAdapter = new AddRoleAdapter(R.layout.item_add_role, mTagDatas);
        mRvTag.setAdapter(mAddRoleAdapter);
        mAddRoleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean selected = mTagDatas.get(position).isSelected();
                if (position == mTagDatas.size() - 1) {
                    Intent intent = new Intent(mContext, MoreTagActivity.class);
                    startActivity(intent);
                } else {
                    mTagDatas.get(position).setSelected(!selected);
                    mAddRoleAdapter.notifyItemChanged(position);
                }
            }
        });

        mRvLike.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager instanceof LinearLayoutManager) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                    LogUtils.i("最后可见条目位置 = " + lastVisibleItemPosition + "; 数据集长度 = " + mStoryTitleLikeAdapter.getData().size());
                    if (lastVisibleItemPosition == mStoryTitleLikeAdapter.getData().size() - 1) {
                        LogUtils.i("获取跟多角色 ====");
                        likePage++;
                        mPresenter.loadMoreLike(likePage, likeSize);
                    }
                }
            }
        });

    }

    private void initToolBar() {
        String name = "千千世界";
        String uId = SCCacheUtils.getCache("0", Constants.CACHE_CUR_USER);
        String spaceInfo = SCCacheUtils.getCache(uId, Constants.CACHE_SPACE_INFO);
        if (!TextUtils.isEmpty(spaceInfo)) {
            SpaceInfo spaceDetailInfo = new Gson().fromJson(spaceInfo, SpaceInfo.class);
            name = spaceDetailInfo.getName();
        }
        mTbStoryTitle.setTitleText(name);
        mTbStoryTitle.setBtnEnabled(false, true);
        TextView titleView = mTbStoryTitle.getTitleView();
        Drawable drawable = getResources().getDrawable(R.mipmap.abs_therrbody_btn_putaway_default);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        titleView.setCompoundDrawables(null, null, drawable, null);
        titleView.setCompoundDrawablePadding(DensityUtils.dip2px(this, 4));
        mTbStoryTitle.setOnTitleClickListener(this);
        mTbStoryTitle.setOnRightClickListener(this);
    }

    @Override
    public void onTitleClick(View v) {
        String characterId = SCCacheUtils.getCacheCharacterId();
        if (TextUtils.isEmpty(characterId)){
            return;
        }

        finish();
        overridePendingTransition(R.anim.activity_enter_alpha, R.anim.activity_anim_default);
    }

    @Override
    public void onRightClick(View v) {
        //添加时空
        Intent intent = new Intent(this, AddNewSpaceActivity.class);
        startActivity(intent);
    }


    @Override
    public void getTagSuccess(List<TagContentBean> tagList) {
        refreshFinish();
        if (tagList == null) {
            return;
        }
        int size = tagList.size();
        if (size > 19) {
            size = 19;
        }

        for (int i = 0; i < size; i++) {
            StoryTagInfo storyTagInfo = new StoryTagInfo();
            storyTagInfo.setTag(tagList.get(i).getTagName());
            storyTagInfo.setTagBean(tagList.get(i));
            mTagDatas.add(storyTagInfo);
        }

        StoryTagInfo storyTagInfo = new StoryTagInfo();
        storyTagInfo.setTag("更多");
        storyTagInfo.setSelected(true);
        mTagDatas.add(storyTagInfo);
        mAddRoleAdapter.notifyDataSetChanged();

    }

    private void loadMore() {
        page++;
        isLoadMore = true;
        mPresenter.loadMoreData(page, size);
    }

    @Override
    public void getSpaceListSuccess(List<SpaceInfo> spaceInfoList, boolean isLast) {
        spaceSuc = true;
        refreshFinish();
        if (spaceInfoList == null) {
            if (isLast) {
                mStagAdapter.loadMoreEnd();
            } else {
                mStagAdapter.loadMoreFail();
            }
        } else {
            if (isLoadMore) {
                if (isLast) {
                    mStagAdapter.loadMoreEnd();
                } else {
                    mStagAdapter.loadMoreComplete();
                }
                mStagAdapter.addData(spaceInfoList);
            } else {
                mStagAdapter.setNewData(spaceInfoList);
                mStagAdapter.disableLoadMoreIfNotFullPage(mRvStoryTitle);
            }
            mStagAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getMyFavoriteSuccess(List<SpaceInfo> favoriteSpaceList, boolean isLast) {
        likeSuc = true;
        refreshFinish();
        if (favoriteSpaceList == null) {
            hideFavoriteLayout();
            return;
        }
        if (favoriteSpaceList.size() == 0&&likeDatas.size() == 0 ) {
                hideFavoriteLayout();
            return;
            }
        showFavoriteLayout();
        likeDatas.addAll(favoriteSpaceList);
        mStoryTitleLikeAdapter.notifyDataSetChanged();
        firstLoadLike = false;
    }

    @Override
    public void loadMoreResult() {
        mRvStoryTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStagAdapter.loadMoreComplete();
            }
        }, 1000);
    }

    private void hideFavoriteLayout() {
        if (mLlFavorite != null) {
            mLlFavorite.setVisibility(View.GONE);
        }
    }

    private void showFavoriteLayout() {
        if (mLlFavorite != null) {
            mLlFavorite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        spaceSuc = likeSuc = false;
        likeDatas.clear();
        page = 0;
        likePage = 0;
        firstLoadLike = true;
        initData();
    }

    private void refreshFinish() {
        if (mSrlStoryTitle != null && spaceSuc && likeSuc) {
            mSrlStoryTitle.setRefreshing(false);
        }
    }

}
