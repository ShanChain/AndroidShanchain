package com.shanchain.arkspot.ui.presenter.impl;

import com.shanchain.arkspot.ui.presenter.RecommendPresenter;
import com.shanchain.arkspot.ui.view.fragment.view.RecommendView;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class RecommendPresenterImpl implements RecommendPresenter {

    private RecommendView mRecommendView;

    public RecommendPresenterImpl(RecommendView recommendView) {
        mRecommendView = recommendView;
    }

    @Override
    public void initData() {


        mRecommendView.initSuccess();
    }
}
