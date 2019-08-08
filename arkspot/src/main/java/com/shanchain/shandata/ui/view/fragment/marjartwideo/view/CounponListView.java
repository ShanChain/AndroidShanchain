package com.shanchain.shandata.ui.view.fragment.marjartwideo.view;

/**
 * Created by WealChen
 * Date : 2019/7/20
 * Describe :
 */
public interface CounponListView {
    void showProgressStart();
    void showProgressEnd();
    void setCounponList(String response,int pullType);
    void setMyGetCounponList(String response,int pullType);
    void setMyCreateCounponList(String response,int pullType);
}
