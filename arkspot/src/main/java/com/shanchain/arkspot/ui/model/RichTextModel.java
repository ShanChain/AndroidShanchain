package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/11/8.
 */

public class RichTextModel {

    private String text;
    private String imgPath;
    private boolean isImg;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isImg() {
        return isImg;
    }

    public void setImg(boolean img) {
        isImg = img;
    }
}
