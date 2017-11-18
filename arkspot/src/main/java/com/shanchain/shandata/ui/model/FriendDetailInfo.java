package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/23.
 */

public class FriendDetailInfo {

    /**
     * characterId : 9
     * userId : 4
     * name : 太乙真人
     * intro : 出自手游《王者荣耀》，作为一名炼金师，太乙真人大部分时间都宅在家里研究各种稀奇古怪的东西，现在是时候把这些发明拿到战场上试验一下威力了~
     * disc :
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg
     * spaceId : 16
     * modelId : 9
     * modelNo : 1
     * signature : 蝴蝶是我，我是蝴蝶
     * status : 1
     * createTime : 1506048534000
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
