package com.shanchain.shandata.ui.view.activity.jmessageui.view;

/**
 * Created by WealChen
 * Date : 2019/9/4
 * Describe :
 */
public interface GroupMenberView {
    void showProgressStart();
    void showProgressEnd();
    void setGroupMenberResponse(String response,int pullType);
    void setCheckGroupCreateeResponse(String response);
    void setDeleteGroupMenberResponse(String response);
}
