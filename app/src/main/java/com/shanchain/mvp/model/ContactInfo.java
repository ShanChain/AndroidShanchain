package com.shanchain.mvp.model;

import java.io.Serializable;

/**
 * Created by 周建 on 2017/5/31.
 */

public class ContactInfo implements Serializable{
    private String avatar;
    private String name;
    private String phone;
    private String email;
    private String qq;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getQq() {
        return qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }
    @Override
    public String toString() {
        return "ContactInfo [name=" + name + ", phone=" + phone + ", email="
                + email + ", qq=" + qq + "]";
    }
}
