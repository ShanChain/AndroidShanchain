package com.shanchain.arkspot.ui.view.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanchain.arkspot.R;
import com.shanchain.arkspot.base.BaseFragment;
import com.shanchain.arkspot.ui.model.StoryBeanModel;
import com.shanchain.arkspot.ui.presenter.RecommendPresenter;
import com.shanchain.arkspot.ui.presenter.impl.RecommendPresenterImpl;
import com.shanchain.arkspot.ui.view.fragment.view.RecommendView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by zhoujian on 2017/8/23.
 */

public class RecommendedFragment extends BaseFragment implements RecommendView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.rv_fragment_recommend)
    RecyclerView mRvFragmentRecommend;
    @Bind(R.id.srl_fragment_recommend)
    SwipeRefreshLayout mSrlFragmentRecommend;
    private RecommendPresenter mPresenter;
    private int page = 0;
    private int size = 10;
    @Override
    public View initView() {
        return View.inflate(mActivity, R.layout.fragment_recommended, null);
    }

    @Override
    public void initData() {
        mPresenter = new RecommendPresenterImpl(this);
        mSrlFragmentRecommend.setColorSchemeColors(getResources().getColor(R.color.colorActive));
        mSrlFragmentRecommend.setOnRefreshListener(this);
        //mSrlFragmentRecommend.setRefreshing(true);
        mPresenter.initData(page,size);
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void initSuccess(List<StoryBeanModel> list, boolean isLast) {

    }
}
