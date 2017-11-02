package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/27.
 */

public class ResponseFocusData {


    /**
     * characterId : 128
     * createTime : 1509091283000
     * disc :
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg
     * intro : 儒门有志羁风雨　　失鹿山河散若星千古文人侠客梦　　肯将碧血写丹青
     * modelId : 135
     * modelNo : 1
     * name : 长歌
     * signature :
     * spaceId : 17
     * status : 0
     * userId : 4
     */

    private int characterId;
    private long createTime;
    private String disc;
    private String headImg;
    private String intro;
    private int modelId;
    private int modelNo;
    private String name;
    private String signature;
    private int spaceId;
    private int status;
    private int userId;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

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

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getModelNo() {
        return modelNo;
    }

    public void setModelNo(int modelNo) {
        this.modelNo = modelNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
