package com.shanchain.arkspot.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoujian on 2017/10/16.
 */

public class FavoriteSpaceBean implements Serializable{

    /**
     * spaceId : 2
     * name : 夏商周
     * slogan : 文明的少年时代
     * intro : 夏商周三代英雄辈出，文明的基因逐渐形成
     * disc : 我的
     * bgPic : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/cf174fdcc3d147d29ac253f3634a8230.jpg
     * status : 0
     * createBy : 1
     * createTime : 1505876061000
     * updateTime : 1507620140000
     * characterModelNum : 0
     * favoriteNum : 3
     * tagMap : []
     */

    private int spaceId;
    private String name;
    private String slogan;
    private String intro;
    private String disc;
    private String bgPic;
    private int status;
    private int createBy;
    private long createTime;
    private long updateTime;
    private int characterModelNum;
    private int favoriteNum;
    private List<?> tagMap;


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

    public String getBgPic() {
        return bgPic;
    }

    public void setBgPic(String bgPic) {
        this.bgPic = bgPic;
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

    public List<?> getTagMap() {
        return tagMap;
    }

    public void setTagMap(List<String> tagMap) {
        this.tagMap = tagMap;
    }


    @Override
    public String toString() {
        return "FavoriteSpaceBean{" +
                "spaceId=" + spaceId +
                ", name='" + name + '\'' +
                ", slogan='" + slogan + '\'' +
                ", intro='" + intro + '\'' +
                ", disc='" + disc + '\'' +
                ", bgPic='" + bgPic + '\'' +
                ", status=" + status +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", characterModelNum=" + characterModelNum +
                ", favoriteNum=" + favoriteNum +
                ", tagMap=" + tagMap +
                '}';
    }
}
