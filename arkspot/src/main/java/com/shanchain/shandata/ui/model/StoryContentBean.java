package com.shanchain.shandata.ui.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhoujian on 2017/11/21.
 */

public class StoryContentBean implements MultiItemEntity{
    /**
     * characterId : 11
     * commentCount : 2
     * content : 第一个故事的第一个续集第二个故事
     * createTime : 1508762821000
     * genNum : 2
     * intro : 第四个故事
     * lineNum : 1
     * locNum : 2
     * parentForkId : 0
     * rootId : 1
     * spaceId : 16
     * status : 1
     * storyId : 5
     * supportCount : 1
     * title :  picstory
     * transpond : 1
     * type : 1
     * updateTime : 1509703031000
     * userId : 25
     */

    private int characterId;
    private int commentCount;
    private String content;
    private long createTime;
    private int genNum;
    private String intro;
    private int lineNum;
    private int locNum;
    private int parentForkId;
    private int rootId;
    private int spaceId;
    private int status;
    private int storyId;
    private int supportCount;
    private String title;
    private int transpond;
    private int type;
    private long updateTime;
    private int userId;
    private ContactBean contactBean;
    private boolean fav;

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getGenNum() {
        return genNum;
    }

    public void setGenNum(int genNum) {
        this.genNum = genNum;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public int getLocNum() {
        return locNum;
    }

    public void setLocNum(int locNum) {
        this.locNum = locNum;
    }

    public int getParentForkId() {
        return parentForkId;
    }

    public void setParentForkId(int parentForkId) {
        this.parentForkId = parentForkId;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
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

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
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

    public int getTranspond() {
        return transpond;
    }

    public void setTranspond(int transpond) {
        this.transpond = transpond;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public ContactBean getContactBean() {
        return contactBean;
    }

    public void setContactBean(ContactBean contactBean) {
        this.contactBean = contactBean;
    }
}
