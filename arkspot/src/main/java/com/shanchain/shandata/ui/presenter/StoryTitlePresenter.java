package com.shanchain.shandata.ui.presenter;

/**
 * Created by zhoujian on 2017/10/13.
 */

public interface StoryTitlePresenter {
    void initFavData(int page,int size);

    void loadMoreData(int page, int size);

    void initSpace(int page, int size);

    void loadMoreLike(int likePage, int likeSize);
}
