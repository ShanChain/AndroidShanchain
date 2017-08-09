package com.shanchain.shandata.mvp.view.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanchain.shandata.R;
import com.shanchain.shandata.adapter.FoundListAdapter;
import com.shanchain.shandata.adapter.FoundNightMarketAdapter;
import com.shanchain.shandata.adapter.FoundStoryAdapter;
import com.shanchain.shandata.base.BaseFragment;
import com.shanchain.shandata.mvp.model.FoundGoodStoryInfo;
import com.shanchain.shandata.mvp.model.FoundNightMarketInfo;
import com.shanchain.shandata.mvp.view.activity.WebActivity;
import com.shanchain.shandata.mvp.view.activity.found.ConvenientlyPublicWelfareActivity;
import com.shanchain.shandata.mvp.view.activity.found.GoodsDetailsActivity;
import com.shanchain.shandata.mvp.view.activity.found.HotNewsActivity;
import com.shanchain.shandata.mvp.view.activity.found.NightMarketActivity;
import com.shanchain.shandata.mvp.view.activity.found.PublicWelfareProjectsActivity;
import com.shanchain.shandata.widgets.other.AutoHeightListView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发现页面
 */

public class FoundFragment extends BaseFragment {
    @Bind(R.id.iv_found_top)
    ImageView mIvFoundTop;
    @Bind(R.id.tv_found_title)
    TextView mTvFoundTitle;
    @Bind(R.id.tv_found_des)
    TextView mTvFoundDes;
    @Bind(R.id.tv_found_good_story)
    TextView mTvFoundGoodStory;
    @Bind(R.id.xrv_found_good_story)
    RecyclerView mXrvFoundGoodStory;
    @Bind(R.id.tv_found_hot_news)
    TextView mTvFoundHotNews;
    @Bind(R.id.iv_found_public_welfare)
    ImageView mIvFoundPublicWelfare;
    @Bind(R.id.tv_found_public_welfare_title)
    TextView mTvFoundPublicWelfareTitle;
    @Bind(R.id.tv_found_public_welfare_des)
    TextView mTvFoundPublicWelfareDes;
    @Bind(R.id.tv_found_night_market)
    TextView mTvFoundNightMarket;
    @Bind(R.id.xrv_found_night_market)
    RecyclerView mXrvFoundNightMarket;
    @Bind(R.id.tv_found_public_welfare)
    TextView mTvFoundPublicWelfare;
    @Bind(R.id.ll_fragment_public_welfare)
    LinearLayout mLlFragmentPublicWelfare;
    @Bind(R.id.ahlv_found)
    AutoHeightListView mAhlvFound;
    private List<FoundNightMarketInfo> mMarketDatas;
    private List<FoundGoodStoryInfo> mStoryDatas;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_found, null);
    }

    @Override
    public void initData() {

        getDataFromService();

        initStoryRecyclerView();

        initListView();

        initNightMarketRecyclerView();
    }

    private void initListView() {

        FoundListAdapter foundListAdapter = new FoundListAdapter();

        mAhlvFound.setAdapter(foundListAdapter);

        mAhlvFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mActivity,WebActivity.class));
            }
        });
    }

    private void initNightMarketRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mXrvFoundNightMarket.setLayoutManager(layoutManager);
        FoundNightMarketAdapter marketAdapter = new FoundNightMarketAdapter(mActivity, R.layout.item_found_market, mMarketDatas);
        mXrvFoundNightMarket.setAdapter(marketAdapter);
        marketAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
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

    private void initStoryRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mXrvFoundGoodStory.setLayoutManager(layoutManager);
        FoundStoryAdapter foundStoryAdapter = new FoundStoryAdapter(mActivity, R.layout.item_found_good_storys, mStoryDatas);
        mXrvFoundGoodStory.setAdapter(foundStoryAdapter);
        foundStoryAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @OnClick({R.id.tv_found_public_welfare, R.id.ll_fragment_public_welfare, R.id.tv_found_hot_news, R.id.tv_found_night_market})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_found_hot_news:
                startActivity(new Intent(mActivity, HotNewsActivity.class));
                break;
            case R.id.tv_found_public_welfare:
                Intent intentPublic = new Intent(mActivity, ConvenientlyPublicWelfareActivity.class);
                startActivity(intentPublic);
                break;
            case R.id.ll_fragment_public_welfare:
                Intent intentPublicProject = new Intent(mActivity, PublicWelfareProjectsActivity.class);
                startActivity(intentPublicProject);
                break;
            case R.id.tv_found_night_market:
                Intent intent = new Intent(mActivity, NightMarketActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void getDataFromService() {
        mMarketDatas = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            FoundNightMarketInfo nightMarketInfo = new FoundNightMarketInfo();
            mMarketDatas.add(nightMarketInfo);
        }

        mStoryDatas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            FoundGoodStoryInfo goodStoryInfo = new FoundGoodStoryInfo();
            mStoryDatas.add(goodStoryInfo);
        }
    }

}
