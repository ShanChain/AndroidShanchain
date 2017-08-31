package com.shanchain.arkspot.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhoujian on 2017/8/30.
 */

public class ContactInfo implements Parcelable{
    private String img;
    private String name;
    private String letter;
    private boolean isSelected;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
