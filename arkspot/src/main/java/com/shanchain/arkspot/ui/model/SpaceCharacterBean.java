package com.shanchain.arkspot.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoujian on 2017/10/24.
 */

public class SpaceCharacterBean implements Serializable{

    /**
     * modelId : 97
     * name : 叶修
     * intro : 人称荣耀教科书、战术大师，荣耀网游第一批玩家，荣耀职业联赛初代选手，并创造了许多荣耀网游乃至赛场上的战术。
     * disc :
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg
     * status : 1
     * characterNum : 0
     * supportNum : 0
     * parentId : 0
     * createBy : 1
     * createTime : 1505891353000
     * spaceId : 10
     * tagMap : []
     */

    private int modelId;
    private String name;
    private String intro;
    private String disc;
    private String headImg;
    private int status;
    private int characterNum;
    private int supportNum;
    private int parentId;
    private int createBy;
    private long createTime;
    private int spaceId;
    private List<TagContentBean> tagMap;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCharacterNum() {
        return characterNum;
    }

    public void setCharacterNum(int characterNum) {
        this.characterNum = characterNum;
    }

    public int getSupportNum() {
        return supportNum;
    }

    public void setSupportNum(int supportNum) {
        this.supportNum = supportNum;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public List<TagContentBean> getTagMap() {
        return tagMap;
    }

    public void setTagMap(List<TagContentBean> tagMap) {
        this.tagMap = tagMap;
    }
}
