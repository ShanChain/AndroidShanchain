package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/19.
 */

public class ContactBean {


    /**
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg
     * intro : 长久以来，大陆一直流传着关于灭世魔神王的传说。他象征着绝对的黑暗，要将人间界带入永夜和毁灭之中，而他也许的大魔王的转身。
     * name : 项羽
     * modelNo : 1
     * type : 1
     * characterId : 69
     * userId : 25
     */

    private String headImg;
    private String intro;
    private String name;
    private int modelNo;
    private int type;
    private int characterId;
    private int userId;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getModelNo() {
        return modelNo;
    }

    public void setModelNo(int modelNo) {
        this.modelNo = modelNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
