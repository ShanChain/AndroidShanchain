package com.shanchain.shandata.mvp.view.activity.found;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MarkertBarbecueAdapter;
import com.shanchain.shandata.adapter.MarketBearAdapter;
import com.shanchain.shandata.adapter.MarketOtherAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.FoundNightMarketInfo;
import com.shanchain.shandata.mvp.view.activity.mine.MyReservationActivity;
import com.shanchain.shandata.mvp.view.activity.mine.ShanCoinsActivity;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class NightMarketActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener, ArthurToolBar.OnRightClickListener {

    private static final int TYPE_BEAR = 10;
    private static final int TYPE_BARBECUE = 20;
    private static final int TYPE_OTHER = 30;
    ArthurToolBar mToolbarNightMarket;
    @Bind(R.id.tv_market_shan_coins)
    TextView mTvMarketShanCoins;
    @Bind(R.id.ll_market_shan_coins)
    LinearLayout mLlMarketShanCoins;
    @Bind(R.id.tv_market_exchange_record)
    TextView mTvMarketExchangeRecord;
    @Bind(R.id.ll_market_shan_exchange_record)
    LinearLayout mLlMarketShanExchangeRecord;
    @Bind(R.id.tv_market_my_subscribe)
    TextView mTvMarketMySubscribe;
    @Bind(R.id.ll_market_shan_my_subscribe)
    LinearLayout mLlMarketShanMySubscribe;
    @Bind(R.id.iv_market_cooperation_merchants)
    ImageView mIvMarketCooperationMerchants;
    @Bind(R.id.tv_market_merchants)
    TextView mTvMarketMerchants;
    @Bind(R.id.tv_market_merchants_des)
    TextView mTvMarketMerchantsDes;
    @Bind(R.id.tv_market_merchants_address)
    TextView mTvMarketMerchantsAddress;
    @Bind(R.id.tv_market_merchants_phone)
    TextView mTvMarketMerchantsPhone;
    @Bind(R.id.tv_market_merchants_bear)
    TextView mTvMarketMerchantsBear;
    @Bind(R.id.xrv_market_bear)
    RecyclerView mXrvMarketBear;
    @Bind(R.id.tv_market_merchants_barbecue)
    TextView mTvMarketMerchantsBarbecue;
    @Bind(R.id.xrv_market_barbecue)
    RecyclerView mXrvMarketBarbecue;
    @Bind(R.id.tv_market_merchants_other)
    TextView mTvMarketMerchantsOther;
    @Bind(R.id.xrv_market_other)
    RecyclerView mXrvMarketOther;
    private List<FoundNightMarketInfo> mBearDatas;
    private List<FoundNightMarketInfo> mBarbecueDatas;
    private List<FoundNightMarketInfo> mOtherDatas;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_night_market;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initBearRecyclerView();
        initBarbecueRecycler();
        initOtherRecycler();
    }

    private void initData() {
        mBearDatas = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            FoundNightMarketInfo nightMarketInfo = new FoundNightMarketInfo();
            mBearDatas.add(nightMarketInfo);
        }

        mBarbecueDatas = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            FoundNightMarketInfo nightMarketInfo = new FoundNightMarketInfo();
            mBarbecueDatas.add(nightMarketInfo);
        }

        mOtherDatas = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            FoundNightMarketInfo nightMarketInfo = new FoundNightMarketInfo();
            mOtherDatas.add(nightMarketInfo);
        }

    }

    private void initOtherRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mXrvMarketOther.setLayoutManager(layoutManager);
        MarketOtherAdapter otherAdapter = new MarketOtherAdapter(this, R.layout.item_found_market, mOtherDatas);
        mXrvMarketOther.setAdapter(otherAdapter);
        otherAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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

    }

    private void initBarbecueRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mXrvMarketBarbecue.setLayoutManager(layoutManager);
        MarkertBarbecueAdapter barbecueAdapter = new MarkertBarbecueAdapter(this, R.layout.item_found_market, mBarbecueDatas);
        mXrvMarketBarbecue.setAdapter(barbecueAdapter);
        barbecueAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
    }

    private void initBearRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mXrvMarketBear.setLayoutManager(layoutManager);
        MarketBearAdapter bearAdapter = new MarketBearAdapter(this, R.layout.item_found_market, mBearDatas);
        mXrvMarketBear.setAdapter(bearAdapter);
        bearAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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
    }

    private void initToolBar() {
        mToolbarNightMarket = (ArthurToolBar) findViewById(R.id.toolbar_night_market);
        mToolbarNightMarket.setBtnEnabled(true);
        mToolbarNightMarket.setBtnVisibility(true);
        mToolbarNightMarket.setOnLeftClickListener(this);
        mToolbarNightMarket.setOnRightClickListener(this);
    }

    @OnClick({R.id.ll_market_shan_coins, R.id.ll_market_shan_exchange_record, R.id.ll_market_shan_my_subscribe, R.id.tv_market_merchants_bear, R.id.tv_market_merchants_barbecue, R.id.tv_market_merchants_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_market_shan_coins:
                Intent intentCoins = new Intent(this, ShanCoinsActivity.class);
                intentCoins.putExtra("isShanCoins",true);
                startActivity(intentCoins);
                break;
            case R.id.ll_market_shan_exchange_record:
                    readyGo(ExchangeRecordActivity.class);
                break;
            case R.id.ll_market_shan_my_subscribe:
                    readyGo(MyReservationActivity.class);
                break;
            case R.id.tv_market_merchants_bear:
                Intent bearIntent = new Intent(this,GoodsListActivity.class);
                bearIntent.putExtra("goodsType",TYPE_BEAR);
                startActivity(bearIntent);
                break;
            case R.id.tv_market_merchants_barbecue:
                Intent barbecueIntent = new Intent(this,GoodsListActivity.class);
                barbecueIntent.putExtra("goodsType",TYPE_BARBECUE);
                startActivity(barbecueIntent);
                break;
            case R.id.tv_market_merchants_other:
                Intent otherIntent = new Intent(this,GoodsListActivity.class);
                otherIntent.putExtra("goodsType",TYPE_OTHER);
                startActivity(otherIntent);
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {
        readyGo(MarketSearchActivity.class);
    }
}
