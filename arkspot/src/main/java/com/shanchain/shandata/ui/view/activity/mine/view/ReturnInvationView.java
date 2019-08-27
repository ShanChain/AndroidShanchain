package com.shanchain.shandata.ui.view.activity.mine.view;

/**
 * Created by WealChen
 * Date : 2019/8/23
 * Describe :
 */
public interface ReturnInvationView {
    void showProgressStart();
    void showProgressEnd();
    void setInvationDataResponse(String response);
    void setQuearyInvatRecordResponse(String response,int pullType);
}
