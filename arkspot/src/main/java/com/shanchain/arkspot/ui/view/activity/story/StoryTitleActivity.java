package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.AddRoleAdapter;
import com.shanchain.arkspot.adapter.StoryTitleLikeAdapter;
import com.shanchain.arkspot.adapter.StoryTitleStagAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.FavoriteSpaceBean;
import com.shanchain.arkspot.ui.model.SpaceBean;
import com.shanchain.arkspot.ui.model.StoryTagInfo;
import com.shanchain.arkspot.ui.model.TagContentBean;
import com.shanchain.arkspot.ui.presenter.StoryTitlePresenter;
import com.shanchain.arkspot.ui.presenter.impl.StoryTitlePresenterImpl;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.StoryTitleView;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;
import com.shanchain.data.common.utils.DensityUtils;
import com.shanchain.data.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class StoryTitleActivity extends BaseActivity implements ArthurToolBar.OnTitleClickListener, ArthurToolBar.OnRightClickListener, StoryTitleView {


    @Bind(R.id.et_story_title_search)
    EditText mEtStoryTitleSearch;
    @Bind(R.id.rv_story_title)
    RecyclerView mRvStoryTitle;
    @Bind(R.id.tb_story_title)
    ArthurToolBar mTbStoryTitle;
    private RecyclerView mRvLike;
    private RecyclerView mRvTag;
    private String[] mTags = new String[]{"原创","历史","动漫","游戏","小说","影视","娱乐圈","古风","现代"
            ,"耽美","百合","商界","体育","校园","玄幻","奇幻","武侠","仙侠","科幻","更多"};
    private List<StoryTagInfo> mTagDatas = new ArrayList<>();
    private List<SpaceBean> mStagDatas = new ArrayList<>();
    List<FavoriteSpaceBean> likeDatas = new ArrayList<>();
    private View mHeadView;
    private StoryTitleStagAdapter mStagAdapter;
    private AddRoleAdapter mAddRoleAdapter;
    private StoryTitleLikeAdapter mStoryTitleLikeAdapter;
    private StoryTitlePresenter mStoryTitlePresenter;
    private LinearLayout mLlFavorite;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_story_title;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initRecyclerView();
        initData();
    }

    private void initData() {
        mStoryTitlePresenter = new StoryTitlePresenterImpl(this);
        String userId = "12";
        mStoryTitlePresenter.initData(userId);


    }

    private void initRecyclerView() {
        initHeadView();
        final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //防止位置发生改变
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRvStoryTitle.setLayoutManager(manager);

        mStagAdapter = new StoryTitleStagAdapter(R.layout.item_head_stagger,mStagDatas);
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
                Intent intent = new Intent(StoryTitleActivity.this,ChooseRoleActivity.class);
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

    private void loadMore() {
        String userId = "12";
        mStoryTitlePresenter.loadMoreData(userId);
    }


    private void initHeadView() {
        mHeadView = LayoutInflater.from(this).inflate(R.layout.head_story_title,(ViewGroup)findViewById(android.R.id.content),false);
        mRvLike = (RecyclerView) mHeadView.findViewById(R.id.rv_story_title_head);
        mRvTag = (RecyclerView) mHeadView.findViewById(R.id.rv_story_title_tag);
        mLlFavorite = (LinearLayout) mHeadView.findViewById(R.id.ll_head_favorite);
        LinearLayoutManager likeLayoutManager = new LinearLayoutManager(this);
        likeLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvLike.setLayoutManager(likeLayoutManager);

        mStoryTitleLikeAdapter = new StoryTitleLikeAdapter(R.layout.item_head_like,likeDatas);
        mRvLike.setAdapter(mStoryTitleLikeAdapter);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        mRvTag.setLayoutManager(gridLayoutManager);

        mAddRoleAdapter = new AddRoleAdapter(R.layout.item_add_role, mTagDatas);
        mRvTag.setAdapter(mAddRoleAdapter);
        mAddRoleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boolean selected = mTagDatas.get(position).isSelected();
                if (position == mTagDatas.size()-1){
                    ToastUtils.showToast(StoryTitleActivity.this,"更多");
                }else {
                    mTagDatas.get(position).setSelected(!selected);
                    mAddRoleAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void initToolBar() {
        mTbStoryTitle.setBtnEnabled(false,true);
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
        finish();
        overridePendingTransition(R.anim.activity_enter_alpha, R.anim.activity_anim_default);
    }

    @Override
    public void onRightClick(View v) {
        //添加时空
        Intent intent  = new Intent(this,AddNewSpaceActivity.class);
        startActivity(intent);
    }


    @Override
    public void getTagSuccess(List<TagContentBean> tagList) {
        if (tagList == null){
            return;
        }
        int size = tagList.size();
        if (size>19){
            size = 19;
        }

        for (int i = 0; i < size; i ++) {
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

    @Override
    public void getSpaceListSuccess(List<SpaceBean> spaceBeanList ) {
        if (spaceBeanList == null){
            return;
        }
        mStagDatas.addAll(spaceBeanList);
        mStagAdapter.notifyDataSetChanged();
    }

    @Override
    public void getMyFavoriteSuccess(List<FavoriteSpaceBean> favoriteSpaceList) {
        if (favoriteSpaceList == null){
            hideFavoriteLayout();
            return;
        }
        if (favoriteSpaceList.size() == 0){
            hideFavoriteLayout();
        }
        showFavoriteLayout();
        likeDatas.addAll(favoriteSpaceList);
        mStoryTitleLikeAdapter.notifyDataSetChanged();
    }

    private void hideFavoriteLayout() {
        if (mLlFavorite != null){
            mLlFavorite.setVisibility(View.GONE);
        }
    }

    private void showFavoriteLayout(){
        if (mLlFavorite!=null){
            mLlFavorite.setVisibility(View.VISIBLE);
        }
    }
}
