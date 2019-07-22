package com.shanchain.shandata.ui.view.fragment.marjartwideo.view;

/**
 * Created by WealChen
 * Date : 2019/7/20
 * Describe :
 */
public interface TaskDetailListView {
    void showProgressStart();
    void showProgressEnd();
    void setTaskDetailList(String response,int pullType);
}
