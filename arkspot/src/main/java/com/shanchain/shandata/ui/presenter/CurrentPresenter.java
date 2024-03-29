package com.shanchain.shandata.ui.presenter;

/**
 * Created by zhoujian on 2017/10/17.
 */

public interface CurrentPresenter {

    void initData(int page,int size);

    void storySupport(int position,String storyId);

    void refreshData(int page , int size);

    void loadMore(int page , int size);

    void storyCancelSupport(int position ,String storyId);

    void deleteSelfStory(int position ,String storyId);
}
