package com.shanchain.arkspot.ui.presenter;

/**
 * Created by zhoujian on 2017/10/13.
 */

public interface StoryTitlePresenter {
    void initFavData();

    void loadMoreData(int page, int size);

    void initSpace(int page, int size);
}
