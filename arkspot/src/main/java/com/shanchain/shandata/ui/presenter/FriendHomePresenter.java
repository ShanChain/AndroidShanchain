package com.shanchain.shandata.ui.presenter;

/**
 * Created by zhoujian on 2017/11/14.
 */

public interface FriendHomePresenter {
    void initCharacterInfo(int characterId);

    void focus(int characterId);

    void initStory(int characterId, int page, int size);

    void loadMore(int characterId, int page, int size);

    void obtainHxInfo(int characterId);

    void storyCancelSupport(int position, String substring);


    void storySupport(int position, String substring);

    void focusCancel(int characterId);
}
