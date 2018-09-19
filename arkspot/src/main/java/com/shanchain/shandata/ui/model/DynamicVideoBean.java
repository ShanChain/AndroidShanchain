package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2018/1/2.
 */

public class DynamicVideoBean {


    /**
     * authors : [162,2,3]
     * background : 背景图片
     * characterId : 162
     * commentCount : 0
     * createTime : 1513130718000
     * intro : 简介
     * playId : 3
     * referId : 1
     * referType : story
     * spaceId : 104
     * status : 1
     * supportCount : 0
     * title : 对戏
     * updateTime : 1513131125000
     * url : 2222
     * userId : 100030
     * vidId : 6be491dd282f4cf09965ec772d4d169a
     */

    private String background;
    private int characterId;
    private int commentCount;
    private long createTime;
    private String intro;
    private int playId;
    private int referId;
    private String referType;
    private int spaceId;
    private int status;
    private int supportCount;
    private String title;
    private long updateTime;
    private String url;
    private int userId;
    private String vidId;
    private List<Integer> authors;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getPlayId() {
        return playId;
    }

    public void setPlayId(int playId) {
        this.playId = playId;
    }

    public int getReferId() {
        return referId;
    }

    public void setReferId(int referId) {
        this.referId = referId;
    }

    public String getReferType() {
        return referType;
    }

    public void setReferType(String referType) {
        this.referType = referType;
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

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getVidId() {
        return vidId;
    }

    public void setVidId(String vidId) {
        this.vidId = vidId;
    }

    public List<Integer> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Integer> authors) {
        this.authors = authors;
    }
}
