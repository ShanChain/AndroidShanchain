package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class SpaceDetailInfo {

    /**
     * spaceId : 16
     * name : 王者荣耀
     * slogan : 欢迎来到王者荣耀，敌军还有5秒到达战场！
     * intro : 无限时空中，时光的洪流汇聚于同一片大陆，机关术与魔道肆虐，让世界大地面目全非。
     英雄，那些熟知的名字，不可思议地汇集在一起，而你的到来，将会改变什么？
     * disc : 423234423442342112
     * bgPic : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/f927168343c540ef87f60882530e8dde.jpg
     * status : 1
     * createBy : 1
     * createTime : 1505876096000
     * updateTime : 1507536822000
     * characterModelNum : 11
     * favoriteNum : 2
     * tagMap : [{"tagId":52,"tagName":"手游","rate":0}]
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

    public List<TagContentBean> getTagMap() {
        return tagMap;
    }

    public void setTagMap(List<TagContentBean> tagMap) {
        this.tagMap = tagMap;
    }
}
