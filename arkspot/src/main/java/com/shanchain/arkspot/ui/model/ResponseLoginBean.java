package com.shanchain.arkspot.ui.model;

public class ResponseLoginBean {


    /**
     * account : 13618645040
     * token : 23b79db4cc52d3bd9e8ce193bbc27259
     * userInfo : {"created":1499914956000,"level":0,"loggedIn":1499961756000,"mobile":"13618645040","nickName":"we","sex":0,"userId":18}
     */

    private String account;
    private String token;
    private LoginUserInfoBean userInfo;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginUserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(LoginUserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "ResponseLoginBean{" +
                "account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}
