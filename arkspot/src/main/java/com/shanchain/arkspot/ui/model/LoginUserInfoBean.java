package com.shanchain.arkspot.ui.model;

public class LoginUserInfoBean {

    /**
     * created : 1499914956000
     * level : 0
     * loggedIn : 1499961756000
     * mobile : 13618645040
     * nickName : we
     * sex : 0
     * userId : 18
     */

    private long created;
    private int level;
    private long loggedIn;
    private String mobile;
    private String nickName;
    private int sex;
    private int userId;

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LoginUserInfoBean{" +
                "created=" + created +
                ", level=" + level +
                ", loggedIn=" + loggedIn +
                ", mobile='" + mobile + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", userId=" + userId +
                '}';
    }
}
