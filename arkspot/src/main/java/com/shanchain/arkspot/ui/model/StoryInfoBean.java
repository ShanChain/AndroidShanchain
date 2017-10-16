package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/13.
 */

public class StoryInfoBean {

    /**
     * characterId : 11
     * createTime : 1506048691000
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg
     * intro : 楚汉之地，东方最神秘的地域之一。虔诚的大河子民中，他那样另类：既无视信仰，又热衷利益，更不择手段。
     * modelId : 16
     * modelNo : 2
     * name : 刘邦
     * userId : 4
     */

    private int characterId;
    private long createTime;
    private String headImg;
    private String intro;
    private int modelId;
    private int modelNo;
    private String name;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
