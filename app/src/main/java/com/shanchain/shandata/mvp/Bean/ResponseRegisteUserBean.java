package com.shanchain.shandata.mvp.Bean;

/**
 * Created by zhoujian on 2017/7/12.
 */

public class ResponseRegisteUserBean {

    /**
     * account : 18576640992
     * token : b8b20f7ad11317e9ebe69651a954601b
     * userInfo : {"birthday":1,"country":1,"created":1499913866275,"email":1,"headIcon":1,"idNumber":1,"level":0,"loggedIn":1,"mobile":"18576640992","nickName":"flyye","sex":0,"title":1,"userId":17,"userName":1}
     */

    private String account;
    private String token;
    private RegisteUserInfoBean userInfo;

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

    public RegisteUserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(RegisteUserInfoBean userInfo) {
        this.userInfo = userInfo;
    }
}
