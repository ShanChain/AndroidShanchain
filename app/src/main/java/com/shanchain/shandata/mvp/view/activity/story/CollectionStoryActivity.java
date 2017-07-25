package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.CollectionStoryAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.CollectionStoryInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CollectionStoryActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarCollectionStory;
    @Bind(R.id.xrv_collection_story)
    XRecyclerView mXrvCollectionStory;
    private List<CollectionStoryInfo> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_collection_story;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mXrvCollectionStory.setLayoutManager(new LinearLayoutManager(this));
        mXrvCollectionStory.setLoadingMoreEnabled(false);
        mXrvCollectionStory.setPullRefreshEnabled(false);
        mXrvCollectionStory.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorAddFriendDivider)));
        CollectionStoryAdapter adapter = new CollectionStoryAdapter(this,R.layout.item_collection_story,mDatas);
        mXrvCollectionStory.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(CollectionStoryActivity.this,StoryHomePagerActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 5; i ++) {
            CollectionStoryInfo collectionStoryInfo = new CollectionStoryInfo();
            mDatas.add(collectionStoryInfo);
        }


    }

    private void initToolBar() {
        mToolbarCollectionStory = (ArthurToolBar) findViewById(R.id.toolbar_collection_story);
        mToolbarCollectionStory.setBtnEnabled(true,false);
        mToolbarCollectionStory.setBtnVisibility(true,false);
        mToolbarCollectionStory.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
