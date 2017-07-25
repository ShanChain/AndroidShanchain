package com.shanchain.shandata.mvp.view.activity.mine;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MyReservationAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.ReservationInfo;
import com.shanchain.shandata.mvp.view.activity.found.NightMarketActivity;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

public class MyReservationActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {


    ArthurToolBar mToolbarMyReservation;
    @Bind(R.id.xrv_my_reservation)
    XRecyclerView mXrvMyReservation;
    @Bind(R.id.activity_my_reservation)
    LinearLayout mActivityMyReservation;
    private List<ReservationInfo> datas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_my_reservation;
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
        mXrvMyReservation.setLayoutManager(linearLayoutManager);

        mXrvMyReservation.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorListDivider)));

        mXrvMyReservation.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXrvMyReservation.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mXrvMyReservation.setLoadingMoreEnabled(false);
        MyReservationAdapter reservationAdapter = new MyReservationAdapter(this,R.layout.item_reservation,datas);
        mXrvMyReservation.setAdapter(reservationAdapter);
        mXrvMyReservation.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXrvMyReservation.refreshComplete();
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
        for (int i = 0; i < 5; i ++) {
            ReservationInfo reservationInfo = new ReservationInfo();
            reservationInfo.setGoods("啤酒");
            reservationInfo.setOrderNum(new Random().nextInt(50000)*123 + "");
            reservationInfo.setExchangeCode(new Random().nextInt(60000)*543 + "");
            datas.add(reservationInfo);
        }

    }

    private void initToolBar() {
        mToolbarMyReservation = (ArthurToolBar) findViewById(R.id.toolbar_my_reservation);
        mToolbarMyReservation.setBtnEnabled(true);
        mToolbarMyReservation.setBtnVisibility(true);
        mToolbarMyReservation.setOnRightClickListener(this);
        mToolbarMyReservation.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        //夜市
        readyGo(NightMarketActivity.class);
    }
}
