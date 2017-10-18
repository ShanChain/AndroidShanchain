package com.shanchain.arkspot.ui.view.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.adapter.CurrentAdapter;
import com.shanchain.arkspot.base.BaseFragment;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.presenter.CurrentPresenter;
import com.shanchain.arkspot.ui.presenter.impl.CurrentPresenterImpl;
import com.shanchain.arkspot.ui.view.fragment.view.CurrentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class CurrentFragment extends BaseFragment implements CurrentView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_story_current)
    RecyclerView mRvStoryCurrent;
    @Bind(R.id.srl_story_current)
    SwipeRefreshLayout mSrlStoryCurrent;
    private CurrentPresenter mCurrentPresenter;
    List<StoryBeanModel> datas = new ArrayList<>();
    private CurrentAdapter mAdapter;

    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_current, null);
    }

    @Override
    public void initData() {
        mSrlStoryCurrent.setOnRefreshListener(this);
        mSrlStoryCurrent.setColorSchemeColors(getResources().getColor(R.color.colorActive));
        mSrlStoryCurrent.setRefreshing(true);
        mCurrentPresenter = new CurrentPresenterImpl(this);
        String characterId = "12";
        mCurrentPresenter.initData(characterId, 0, 100, "16");

        initRecyclerView();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRvStoryCurrent.setLayoutManager(layoutManager);
        mAdapter = new CurrentAdapter(datas);
        mRvStoryCurrent.setAdapter(mAdapter);

    }


    @Override
    public void initSuccess(List<StoryBeanModel> list) {
        mSrlStoryCurrent.setRefreshing(false);
        if (datas == null) {

            return;
        } else {

            datas.addAll(list);
            mAdapter.notifyDataSetChanged();


        }

    }

    @Override
    public void onRefresh() {
        mSrlStoryCurrent.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSrlStoryCurrent.setRefreshing(false);
            }
        }, 1000);
    }
}
