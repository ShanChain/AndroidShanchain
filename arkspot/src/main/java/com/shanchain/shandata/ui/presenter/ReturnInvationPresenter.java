package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/8/23
 * Describe :
 */
public interface ReturnInvationPresenter {
    void getInvationDataFromUser(String userId);
    void queryUserInvationRecord(String userId,int page,int size,int pullType);//查询邀请记录
}
