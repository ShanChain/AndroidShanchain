package com.shanchain.shandata.ui.presenter;

/**
 * Created by WealChen
 * Date : 2019/7/20
 * Describe :马甲券列表接口类
 */
public interface CouponListPresenter {
    //获取马甲券列表
    void getCounponList(String subuserId,int page,int size,int pullType);

}
