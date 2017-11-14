package com.shanchain.arkspot.ui.presenter;

public interface AttentionPresenter {

    void initData(int page,int size);

    void refresh(int page, int size);

    void loadMore(int page, int size);

    void storyCancelSupport(int position, String storyId);

    void storySupport(int position, String storyId);
}
