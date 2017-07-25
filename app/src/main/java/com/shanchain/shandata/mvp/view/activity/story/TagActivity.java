package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.TagAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.TagInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TagActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    @Bind(R.id.toolbar_tag)
    ArthurToolBar mToolbarTag;
    @Bind(R.id.xrv_tag)
    XRecyclerView mXrvTag;
    private List<TagInfo> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_tag;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mXrvTag.setLayoutManager(new LinearLayoutManager(this));
        mXrvTag.setLoadingMoreEnabled(false);
        mXrvTag.setPullRefreshEnabled(false);

        mXrvTag.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorAddFriendDivider)));

        TagAdapter tagAdapter = new TagAdapter(this,R.layout.item_hot_tag,mDatas);
        mXrvTag.setAdapter(tagAdapter);

        tagAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(TagActivity.this,StoryListActivity.class);
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
        for (int i = 0; i < 13; i ++) {
            TagInfo tagInfo = new TagInfo();
            tagInfo.setTagName("标签" + i);
            tagInfo.setStoryCount(423 + i*3);
            mDatas.add(tagInfo);
        }
    }

    private void initToolBar() {
        mToolbarTag = (ArthurToolBar) findViewById(R.id.toolbar_tag);
        mToolbarTag.setBtnEnabled(true,false);
        mToolbarTag.setBtnVisibility(true,false);
        mToolbarTag.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
