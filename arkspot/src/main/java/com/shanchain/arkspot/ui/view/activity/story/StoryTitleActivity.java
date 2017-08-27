package com.shanchain.arkspot.ui.view.activity.story;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.StoryTitleGridAdapter;
import com.shanchain.arkspot.adapter.StoryTitleLikeAdapter;
import com.shanchain.arkspot.adapter.StoryTitleStagAdapter;
import com.shanchain.arkspot.base.BaseActivity;
import com.shanchain.arkspot.ui.model.StoryLikeInfo;
import com.shanchain.arkspot.ui.model.StoryTagInfo;
import com.shanchain.arkspot.ui.model.StoryTitleStagInfo;
import com.shanchain.arkspot.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import utils.DensityUtils;
import utils.ToastUtils;


public class StoryTitleActivity extends BaseActivity implements ArthurToolBar.OnTitleClickListener {


    @Bind(R.id.et_story_title_search)
    EditText mEtStoryTitleSearch;
    @Bind(R.id.rv_story_title)
    RecyclerView mRvStoryTitle;
    @Bind(R.id.tb_story_title)
    ArthurToolBar mTbStoryTitle;
    private RecyclerView mRvLike;
    private GridView mGrid;
    private String[] mTags;
    private List<StoryTagInfo> mTagDatas;
    private List<StoryTitleStagInfo> mStagDatas;
    private View mHeadView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_story_title;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        initHeadView();
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRvStoryTitle.setLayoutManager(manager);
        initStaggeredData();

        StoryTitleStagAdapter stagAdapter = new StoryTitleStagAdapter(R.layout.item_head_stagger,mStagDatas);
        stagAdapter.addHeaderView(mHeadView);

        mRvStoryTitle.setAdapter(stagAdapter);

        stagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StoryTitleActivity.this,ChooseRoleActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initStaggeredData() {
        mStagDatas = new ArrayList<>();
        for (int i = 0; i < 16; i ++) {
            StoryTitleStagInfo titleStagInfo = new StoryTitleStagInfo();
            if (i % 5== 0) {
                titleStagInfo.setImg(R.drawable.photo_city);
            }else if (i%5 == 1) {
                titleStagInfo.setImg(R.drawable.photo_bear);
            }else if (i%5 == 2) {
                titleStagInfo.setImg(R.drawable.photo_heart);
            }else if (i%5 == 3) {
                titleStagInfo.setImg(R.drawable.photo_public);
            }else if (i%5 == 4) {
                titleStagInfo.setImg(R.drawable.photo_yue);
            }
            titleStagInfo.setTitle("侠客岛");
            titleStagInfo.setDes("不是所有的牛奶都叫旺仔牛奶");
            mStagDatas.add(titleStagInfo);
        }

    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(this).inflate(R.layout.head_story_title,(ViewGroup)findViewById(android.R.id.content),false);
        mRvLike = (RecyclerView) mHeadView.findViewById(R.id.rv_story_title_head);
        mGrid = (GridView) mHeadView.findViewById(R.id.rv_story_title_grid);

        LinearLayoutManager likeLayoutManager = new LinearLayoutManager(this);
        likeLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvLike.setLayoutManager(likeLayoutManager);
        List<StoryLikeInfo> likeDatas = new ArrayList<>();
        for (int i = 0; i < 16; i ++) {
            StoryLikeInfo storyLikeInfo = new StoryLikeInfo();
            if (i%5 == 0) {
                storyLikeInfo.setImg(R.drawable.photo_yue);
            }else if (i%5 == 1) {
                storyLikeInfo.setImg(R.drawable.photo_city);
            }else if (i%5 == 2) {
                storyLikeInfo.setImg(R.drawable.photo_heart);
            }else if (i%5 == 3) {
                storyLikeInfo.setImg(R.drawable.photo_bear);
            }else if (i%5 == 4) {
                storyLikeInfo.setImg(R.drawable.photo_public);
            }
            storyLikeInfo.setTitle("最美少年事");
            likeDatas.add(storyLikeInfo);
        }
        StoryTitleLikeAdapter storyTitleLikeAdapter = new StoryTitleLikeAdapter(R.layout.item_head_like,likeDatas);
        mRvLike.setAdapter(storyTitleLikeAdapter);

        mTagDatas = new ArrayList<>();

        mTags = new String[]{"原创","历史","动漫","游戏","小说","影视","娱乐圈","古风","现代"
                ,"耽美","百合","商界","体育","校园","玄幻","奇幻","武侠","仙侠","科幻","更多"};

        for (int i = 0; i < 20; i ++) {
            StoryTagInfo storyTagInfo = new StoryTagInfo();
            if (i == 19){
                storyTagInfo.setSelected(true);
            }else {
                storyTagInfo.setSelected(false);
            }
           storyTagInfo.setTag(mTags[i]);
            mTagDatas.add(storyTagInfo);
        }


        final StoryTitleGridAdapter gridAdapter = new StoryTitleGridAdapter( mTagDatas);
        mGrid.setAdapter(gridAdapter);
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == mTagDatas.size()-1){
                    ToastUtils.showToast(StoryTitleActivity.this,"更多");
                }else {
                    StoryTagInfo storyTagInfo = mTagDatas.get(position);
                    boolean selected = storyTagInfo.isSelected();
                    storyTagInfo.setSelected(!selected);
                    gridAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void initToolBar() {
        mTbStoryTitle.setBtnEnabled(false);
        TextView titleView = mTbStoryTitle.getTitleView();
        Drawable drawable = getResources().getDrawable(R.mipmap.abs_therrbody_btn_putaway_default);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        titleView.setCompoundDrawables(null, null, drawable, null);
        titleView.setCompoundDrawablePadding(DensityUtils.dip2px(this, 4));
        mTbStoryTitle.setOnTitleClickListener(this);
    }

    @Override
    public void onTitleClick(View v) {
        finish();
    }
}
