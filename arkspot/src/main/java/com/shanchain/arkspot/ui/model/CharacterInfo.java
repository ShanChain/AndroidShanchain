package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/26.
 */

public class CharacterInfo {
    /**
     * characterId : 103
     * userId : 4
     * name : 墨子
     * intro : 上古时代创造了极为辉煌的文明，其中最伟大的发明之一便是机关术。但随着上古时代的终结，机关术的奥秘也逐渐湮没。谁也没想到的是，机关术重新复兴，是通过一名普通的平民工匠之手。
     * disc :
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg
     * spaceId : 16
     * modelId : 12
     * modelNo : 3
     * signature :
     * status : 1
     * createTime : 1509006070069
     */

    private int characterId;
    private int userId;
    private String name;
    private String intro;
    private String disc;
    private String headImg;
    private int spaceId;
    private int modelId;
    private int modelNo;
    private String signature;
    private int status;
    private long createTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
