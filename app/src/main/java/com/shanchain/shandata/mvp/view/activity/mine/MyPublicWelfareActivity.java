package com.shanchain.shandata.mvp.view.activity.mine;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MyPublicWelfareAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.MyPublicWelfareInfo;
import com.shanchain.shandata.mvp.view.activity.found.ConvenientlyPublicWelfareActivity;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MyPublicWelfareActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    ArthurToolBar mToolbarMyPublicWelfare;
    @Bind(R.id.xrv_my_public_welfare)
    XRecyclerView mXrvMyPublicWelfare;
    @Bind(R.id.activity_my_public_welfare)
    LinearLayout mActivityMyPublicWelfare;
    private List<MyPublicWelfareInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_public_welfare;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mXrvMyPublicWelfare.setLayoutManager(linearLayoutManager);

        mXrvMyPublicWelfare.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));

        mXrvMyPublicWelfare.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvMyPublicWelfare.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);

        MyPublicWelfareAdapter adapter = new MyPublicWelfareAdapter(this,R.layout.item_public_welfare,datas);

        mXrvMyPublicWelfare.setLoadingMoreEnabled(false);
        mXrvMyPublicWelfare.setAdapter(adapter);
        mXrvMyPublicWelfare.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXrvMyPublicWelfare.refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    private void initData() {
        datas = new ArrayList<>();
        for (int i = 0; i < 4; i ++) {
            MyPublicWelfareInfo publicWelfareInfo = new MyPublicWelfareInfo();
            publicWelfareInfo.setPubWelfare("1000本图书进校园");
            publicWelfareInfo.setTime("6月14日-6月20日");
            publicWelfareInfo.setShanVouchers("-80善券");
            datas.add(publicWelfareInfo);
        }


    }

    private void initToolBar() {
        mToolbarMyPublicWelfare = (ArthurToolBar) findViewById(R.id.toolbar_my_public_welfare);
        mToolbarMyPublicWelfare.setBtnEnabled(true);
        mToolbarMyPublicWelfare.setBtnVisibility(true);
        mToolbarMyPublicWelfare.setOnLeftClickListener(this);
        mToolbarMyPublicWelfare.setOnRightClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(ConvenientlyPublicWelfareActivity.class);
    }
}
