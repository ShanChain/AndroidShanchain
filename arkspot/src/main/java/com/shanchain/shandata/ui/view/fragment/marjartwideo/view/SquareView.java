package com.shanchain.shandata.ui.view.fragment.marjartwideo.view;

/**
 * Created by WealChen
 * Date : 2019/7/23
 * Describe :
 */
public interface SquareView {
    void showProgressStart();
    void showProgressEnd();
    void setListDataResponse(String response,int pullType);
    void setAttentionResponse(String response,int type);//0加关注，1删除关注
    void setPraiseResponse(String response,int type);//0点赞，1删除点赞
    void setHxUseridResponse(String response);
}
