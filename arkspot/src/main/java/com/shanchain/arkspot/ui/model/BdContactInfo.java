package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/28.
 */

public class BdContactInfo {

    private boolean isGroup;
    private ContactBean mContactBean;
    private ResponseHxUerBean mHxUerBean;
    private GroupInfo mGroupInfo;

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public ContactBean getContactBean() {
        return mContactBean;
    }

    public void setContactBean(ContactBean contactBean) {
        mContactBean = contactBean;
    }

    public ResponseHxUerBean getHxUerBean() {
        return mHxUerBean;
    }

    public void setHxUerBean(ResponseHxUerBean hxUerBean) {
        mHxUerBean = hxUerBean;
    }

    public GroupInfo getGroupInfo() {
        return mGroupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        mGroupInfo = groupInfo;
    }
}
