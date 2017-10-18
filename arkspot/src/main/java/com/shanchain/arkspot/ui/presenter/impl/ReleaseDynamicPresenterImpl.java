package com.shanchain.arkspot.ui.presenter.impl;

import com.shanchain.arkspot.ui.presenter.ReleaseDynamicPresenter;
import com.shanchain.arkspot.ui.view.activity.story.stroyView.ReleaseDynamicView;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class ReleaseDynamicPresenterImpl implements ReleaseDynamicPresenter {

    private ReleaseDynamicView mReleaseDynamicView;

    public ReleaseDynamicPresenterImpl(ReleaseDynamicView releaseDynamicView) {
        mReleaseDynamicView = releaseDynamicView;
    }

    @Override
    public void releaseDynamic(String content) {

    }
}
