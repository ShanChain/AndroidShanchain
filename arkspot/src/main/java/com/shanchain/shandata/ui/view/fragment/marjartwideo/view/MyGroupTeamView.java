package com.shanchain.shandata.ui.view.fragment.marjartwideo.view;

/**
 * Created by WealChen
 * Date : 2019/8/10
 * Describe :
 */
public interface MyGroupTeamView {
    void showProgressStart();
    void showProgressEnd();
    void setQuearyMygoupTeamResponse(String response,int pullType);
    void setCheckPasswResponse(String response);
    void setCheckPassFaile();
    void setAddMinigRoomResponse(String response);
    void setdeleteDigiRoomIdResponse(String response);
    void setUpdateMiningRoomResponse(String response);
    void setCheckUserHasWalletResponse(String response);
}
