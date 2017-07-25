package com.shanchain.shandata.mvp.model;


public class ResponseSmsBean {

    /**
     * mobile : 188888888881
     * smsVerifyCode : 143824
     */

    private String mobile;
    private String smsVerifyCode;
    private String mail;
    private String mailVerifyCode;

    public String getMailVerifyCode() {
        return mailVerifyCode;
    }

    public void setMailVerifyCode(String mailVerifyCode) {
        this.mailVerifyCode = mailVerifyCode;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsVerifyCode() {
        return smsVerifyCode;
    }

    public void setSmsVerifyCode(String smsVerifyCode) {
        this.smsVerifyCode = smsVerifyCode;
    }
}
