package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/11/2.
 */

public class BdAtContactInfo {
    private boolean isAt;
    private ResponseHxUerBean mHxUerBean;
    private ContactInfo mContactInfo;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public ContactInfo getContactInfo() {
        return mContactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        mContactInfo = contactInfo;
    }


    public boolean isAt() {
        return isAt;
    }

    public void setAt(boolean at) {
        isAt = at;
    }

    public ResponseHxUerBean getHxUerBean() {
        return mHxUerBean;
    }

    public void setHxUerBean(ResponseHxUerBean hxUerBean) {
        mHxUerBean = hxUerBean;
    }


}
