package com.shanchain.shandata.mvp.view.activity.story;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ThingsRecordAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.StoryPagerListInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ThingsRecordActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarThingsRecord;
    @Bind(R.id.xrv_things_record)
    XRecyclerView mXrvThingsRecord;
    private List<StoryPagerListInfo> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_things_record;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mXrvThingsRecord.setLayoutManager(new LinearLayoutManager(this));
        mXrvThingsRecord.setPullRefreshEnabled(false);
        mXrvThingsRecord.setLoadingMoreEnabled(false);

        mXrvThingsRecord.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorAddFriendDivider)));

        ThingsRecordAdapter adapter = new ThingsRecordAdapter(this,R.layout.item_things_record,mDatas);
        mXrvThingsRecord.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(ThingsRecordActivity.this,RecordStoryFragmentActivity.class);
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
        for (int i = 0; i < 32; i ++) {
            StoryPagerListInfo listInfo = new StoryPagerListInfo();
            mDatas.add(listInfo);
        }
    }

    private void initToolBar() {
        mToolbarThingsRecord = (ArthurToolBar) findViewById(R.id.toolbar_things_record);
        mToolbarThingsRecord.setBtnEnabled(true,false);
        mToolbarThingsRecord.setBtnVisibility(true,false);
        mToolbarThingsRecord.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
