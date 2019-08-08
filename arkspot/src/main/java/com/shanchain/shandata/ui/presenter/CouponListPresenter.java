package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/7/20
 * Describe :马甲券列表接口类
 */
public interface CouponListPresenter {
    //获取马甲券列表
    void getCounponList(String subuserId,int page,int size,int pullType);
    //获取我领取的马甲券列表
    void getMyGetCounponList(String subuserId,int page,int size,int pullType);
    //获取我创建的马甲券列表
    void getMyCreateCounponList(String subuserId,int page,int size,int pullType);
}
