package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/7/4.
 */

public class ThirdUserInfo {
   // private String token;
    private String nickName;
    private int sex;
    private String headIcon;
   // private String openId;
   // private String accessToken;


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }
}
