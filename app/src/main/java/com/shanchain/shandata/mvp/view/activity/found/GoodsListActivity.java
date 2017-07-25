package com.shanchain.shandata.mvp.view.activity.found;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.GoodsListAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.FoundNightMarketInfo;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class GoodsListActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, XRecyclerView.LoadingListener {

    private static final int TYPE_BEAR = 10;
    private static final int TYPE_BARBECUE = 20;
    private static final int TYPE_OTHER = 30;

    ArthurToolBar mToolbarGoodsList;
    @Bind(R.id.xrv_goods_list)
    XRecyclerView mXrvGoodsList;
    @Bind(R.id.activity_goods_list)
    LinearLayout mActivityGoodsList;
    private List<FoundNightMarketInfo> mDatas;
    private int mGoodsType;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_goods_list;
    }

    @Override
    protected void initViewsAndEvents() {
        mGoodsType = getIntent().getIntExtra("goodsType",TYPE_BEAR);

        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        mXrvGoodsList.setLayoutManager(gridLayoutManager);
        GoodsListAdapter goodsListAdapter = new GoodsListAdapter(this,R.layout.item_goods_list,mDatas);

        mXrvGoodsList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvGoodsList.setLoadingMoreEnabled(false);
        mXrvGoodsList.setAdapter(goodsListAdapter);
        goodsListAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, GoodsDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mXrvGoodsList.setLoadingListener(this);

    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 15; i ++) {
            FoundNightMarketInfo nightMarketInfo = new FoundNightMarketInfo();
            mDatas.add(nightMarketInfo);
        }
    }

    private void initToolBar() {
        mToolbarGoodsList = (ArthurToolBar) findViewById(R.id.toolbar_goods_list);

        switch (mGoodsType) {
            case TYPE_BEAR:
                        mToolbarGoodsList.setTitleText("啤酒");
                break;

            case TYPE_BARBECUE:
                mToolbarGoodsList.setTitleText("烤串");
                break;

            case TYPE_OTHER:
                mToolbarGoodsList.setTitleText("其他");
                break;

        }

        mToolbarGoodsList.setBtnEnabled(true,false);
        mToolbarGoodsList.setBtnVisibility(true,false);
        mToolbarGoodsList.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mXrvGoodsList.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {

    }
}
