package com.shanchain.shandata.mvp.view.activity.found;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.MarketSearchAdapter;
import com.shanchain.shandata.base.BaseActivity;
import com.shanchain.shandata.mvp.model.MarketSearchInfo;
import com.shanchain.shandata.utils.DensityUtils;
import com.shanchain.shandata.widgets.other.RecyclerViewDivider;
import com.shanchain.shandata.widgets.toolBar.ArthurToolBar;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MarketSearchActivity extends BaseActivity implements ArthurToolBar.OnLeftClickListener {

    ArthurToolBar mToolbarSearchMarket;
    @Bind(R.id.et_market_search)
    EditText mEtMarketSearch;
    @Bind(R.id.xrv_market_search)
    XRecyclerView mXrvMarketSearch;

    private List<MarketSearchInfo> datas;
    private List<MarketSearchInfo> show;
    private MarketSearchAdapter mSearchAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_market_search;
    }

    @Override
    protected void initViewsAndEvents() {
        initToolBar();
        initData();
        initRecyclerView();
        initListener();
    }

    private void initListener() {
        mEtMarketSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().trim();
                show.clear();

                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getGoodsName().contains(search)) {
                        show.add(datas.get(i));
                    }
                }
                mSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyclerView() {
        mXrvMarketSearch.setLayoutManager(new LinearLayoutManager(this));
        mXrvMarketSearch.setLoadingMoreEnabled(false);
        mXrvMarketSearch.setPullRefreshEnabled(false);

        mXrvMarketSearch.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.HORIZONTAL,
                DensityUtils.dip2px(this,1),
                getResources().getColor(R.color.colorAddFriendDivider)));

        mSearchAdapter = new MarketSearchAdapter(this, R.layout.item_search_market, show);
        mXrvMarketSearch.setAdapter(mSearchAdapter);
        mSearchAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void initData() {
        datas = new ArrayList<>();
        show = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            MarketSearchInfo marketSearchInfo = new MarketSearchInfo();
            marketSearchInfo.setDes("由碳原子提供");
            marketSearchInfo.setGoodsName("啤酒" + i);
            marketSearchInfo.setPrice(2);
            datas.add(marketSearchInfo);
        }
        show.addAll(datas);
    }

    private void initToolBar() {
        mToolbarSearchMarket = (ArthurToolBar) findViewById(R.id.toolbar_search_market);
        mToolbarSearchMarket.setBtnVisibility(true, false);
        mToolbarSearchMarket.setBtnEnabled(true, false);
        mToolbarSearchMarket.setOnLeftClickListener(this);
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }
}
