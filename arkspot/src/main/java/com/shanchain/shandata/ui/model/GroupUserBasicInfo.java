package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/9/28.
 */

public class GroupUserBasicInfo implements Serializable{

    /**
     * birthday : 2017-09-21
     * created : 1506094774000
     * idNumber : 000000000000000000
     * level : 99
     * loggedIn : 1506094774000
     * mobile : 80082088201
     * nickName : 管理员1号
     * sex : 1
     * title : 管理好我的魂穿
     * userId : 1
     * userName : 管理员1号
     */

    private String birthday;
    private long created;
    private String idNumber;
    private int level;
    private long loggedIn;
    private String mobile;
    private String nickName;
    private int sex;
    private String title;
    private int userId;
    private String userName;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(long loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
