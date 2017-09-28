package com.shanchain.arkspot.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/9/26.
 * 注册环信用户返回的数据实体
 */

public class ComUserInfo implements Serializable{

    /**
     * characterId : 69
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/696ef4cc7af8484185b43d9b0a0966fb.jpg
     * password : hx1506408454465
     * title : 管理好我的魂穿
     * userName : sc2054380235
     */

    private GroupUserInfo info;
    private String headImg;
    private String password;
    private String title;
    private String userName;

    public GroupUserInfo getInfo() {
        return info;
    }

    public void setInfo(GroupUserInfo info) {
        this.info = info;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
