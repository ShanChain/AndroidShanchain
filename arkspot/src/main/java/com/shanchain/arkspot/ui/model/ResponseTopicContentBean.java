package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/26.
 */

public class ResponseTopicContentBean {

    /**
     * topicId : 10
     * spaceId : 16
     * title : 222
     * intro : 34242
     * background : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg
     * storyNum : 0
     * readNum : 0
     * characterId : 9
     * status : 0
     * createTime : 1508901526000
     * updateTime : 1508924242000
     * tagMap : [{"tagId":19,"tagName":"龙珠","rate":0}]
     */

    private int topicId;
    private int spaceId;
    private String title;
    private String intro;
    private String background;
    private int storyNum;
    private int readNum;
    private int characterId;
    private int status;
    private long createTime;
    private long updateTime;
    private List<TagContentBean> tagMap;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getStoryNum() {
        return storyNum;
    }

    public void setStoryNum(int storyNum) {
        this.storyNum = storyNum;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public List<TagContentBean> getTagMap() {
        return tagMap;
    }

    public void setTagMap(List<TagContentBean> tagMap) {
        this.tagMap = tagMap;
    }
}
