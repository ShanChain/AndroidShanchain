package com.shanchain.shandata.mvp.view.activity.found;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.WaitDonateAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.WaitDonateInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class WaitDonateActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, XRecyclerView.LoadingListener {


    ArthurToolBar mToolbarWaitDonate;
    @Bind(R.id.xrv_wait_donate)
    XRecyclerView mXrvWaitDonate;
    @Bind(R.id.activity_wait_donate)
    LinearLayout mActivityWaitDonate;
    private List<WaitDonateInfo> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_wait_donate;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();

    }

    private void initRecyclerView() {
        mXrvWaitDonate.setLayoutManager(new LinearLayoutManager(this));
        mXrvWaitDonate.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));
        mXrvWaitDonate.setLoadingMoreEnabled(false);
        mXrvWaitDonate.setPullRefreshEnabled(true);
        mXrvWaitDonate.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvWaitDonate.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        WaitDonateAdapter donateAdapter = new WaitDonateAdapter(this,R.layout.item_wait_donate,mDatas);
        mXrvWaitDonate.setAdapter(donateAdapter);
        mXrvWaitDonate.setLoadingListener(this);
        donateAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                readyGo(PublicWelfareProjectsActivity.class);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initData() {
        mDatas = new ArrayList<>();

        for (int i = 0; i < 8; i ++) {
            WaitDonateInfo waitDonateInfo = new WaitDonateInfo();
            mDatas.add(waitDonateInfo);
        }

    }

    private void initToolBar() {
        mToolbarWaitDonate = (ArthurToolBar) findViewById(R.id.toolbar_wait_donate);
        mToolbarWaitDonate.setBtnEnabled(true, false);
        mToolbarWaitDonate.setBtnVisibility(true, false);
        mToolbarWaitDonate.setOnLeftClickListener(this);
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
                mXrvWaitDonate.refreshComplete();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {

    }
}
