package com.shanchain.shandata.ui.presenter;

import android.content.Context;

/**
 * Created by WealChen
 * Date : 2019/8/10
 * Describe :
 */
public interface MyGroupTeamPresenter {
    void queryGroupTeam(String joinUserId,String createUser,String searchString,int page,int size,int pullType);//查询矿区列表
    //验证密码图片
    void checkPasswordToServer(Context context, String file, String value);
    //支付成功后调用加入矿区接口
    void insertMiningRoomByOther(String userId,String diggingsId);
}
