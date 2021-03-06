package com.shanchain.shandata.ui.presenter;

/**
 * Created by zhoujian on 2017/11/14.
 */

public interface DynamicDetailsPresenter {


    void initData(int page, int size, String storyId);

    void addComment(String comment, String storyId);

    void deleteComment( String commentId,int position);

    void support(String storyId);

    void supportCancel(String storyId);

    void initNovelInfo(String storyId);

    void supportCancelComment(int commentId,int position);

    void supportComment(int commentId,int position);
}
