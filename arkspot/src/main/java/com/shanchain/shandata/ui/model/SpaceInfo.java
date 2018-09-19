package com.shanchain.shandata.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoujian on 2017/10/13.
 */

public class SpaceInfo implements Serializable{

    /**
     * spaceId : 32
     * name : 韩娱专区
     * slogan : 这里是韩星们的家！
     * intro : 今天开始我可以爱你么，第一次清楚地感觉不想放弃，爱情似乎要降临，我永远要给你最好地，我可以爱你么？
     * disc :
     * background : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg
     * status : 1
     * createBy : 1
     * createTime : 1505897091000
     * updateTime : 1507959637000
     * characterModelNum : 12
     * favoriteNum : 2
     * tagMap : [{"tagId":31,"tagName":"娱乐圈","rate":0}]
     */

    private int spaceId;
    private String name;
    private String slogan;
    private String intro;
    private String disc;
    private String background;
    private int status;
    private int createBy;
    private long createTime;
    private long updateTime;
    private int characterModelNum;
    private int favoriteNum;
    private List<TagContentBean> tagMap;

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getCharacterModelNum() {
        return characterModelNum;
    }

    public void setCharacterModelNum(int characterModelNum) {
        this.characterModelNum = characterModelNum;
    }

    public int getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(int favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public List<TagContentBean> getTagMap() {
        return tagMap;
    }

    public void setTagMap(List<TagContentBean> tagMap) {
        this.tagMap = tagMap;
    }
}
