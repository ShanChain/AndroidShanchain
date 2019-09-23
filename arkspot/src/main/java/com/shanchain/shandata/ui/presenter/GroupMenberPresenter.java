package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/9/4
 * Describe :
 */
public interface GroupMenberPresenter {
    void getGroupMenberList(String roomId,String count,String page,String size,int pullType);
    void checkIsGroupCreater(String roomId);
    void deleteGroupMenber(String rooId,String menbers);
    void getRoomMenbers(String roomId);
}
