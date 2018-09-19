package com.shanchain.shandata.ui.presenter;

/**
 * Created by zhoujian on 2017/11/20.
 */

public interface SceneDetailsPresenter {
    void getGroupInfo(String toChatName);

    void getUserInfo(String toChatName);

    void leaveGroup(String toChatName);
}
