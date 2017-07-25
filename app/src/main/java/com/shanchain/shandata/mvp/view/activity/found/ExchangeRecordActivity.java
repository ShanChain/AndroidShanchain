package com.shanchain.shandata.mvp.view.activity.found;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.ExchangeRecordAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ExchangeRecordInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ExchangeRecordActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, XRecyclerView.LoadingListener {

    ArthurToolBar mToolbarExchangeRecord;
    @Bind(R.id.xrv_exchange_record)
    XRecyclerView mXrvExchangeRecord;
    private List<ExchangeRecordInfo> mDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_exchange_record;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mXrvExchangeRecord.setLayoutManager(new LinearLayoutManager(this));
        mXrvExchangeRecord.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvExchangeRecord.setLoadingMoreEnabled(false);

        mXrvExchangeRecord.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL, DensityUtils.dip2px(this, 1),
                getResources().getColor(R.color.colorAddFriendDivider)));

        ExchangeRecordAdapter exchangeRecordAdapter = new ExchangeRecordAdapter(this, R.layout.item_exchange_record, mDatas);
        mXrvExchangeRecord.setAdapter(exchangeRecordAdapter);
        mXrvExchangeRecord.setLoadingListener(this);

    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ExchangeRecordInfo exchangeRecordInfo = new ExchangeRecordInfo();
            mDatas.add(exchangeRecordInfo);
        }
    }

    private void initToolBar() {
        mToolbarExchangeRecord = (ArthurToolBar) findViewById(R.id.toolbar_exchange_record);
        mToolbarExchangeRecord.setBtnEnabled(true, false);
        mToolbarExchangeRecord.setBtnVisibility(true, false);
        mToolbarExchangeRecord.setOnLeftClickListener(this);
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
                mXrvExchangeRecord.refreshComplete();
            }
        }, 800);
    }

    @Override
    public void onLoadMore() {

    }
}
