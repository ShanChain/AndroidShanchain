package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/21.
 */

public class RNDataBean {

    /**
     * updateTime : 1508297253000
     * storyNum : 18
     * topicId : 6
     * spaceId : 16
     * tagMap : []
     * title : 一起结婚吧
     * intro : 啦啦啦啦安拉啊
     * status : 1
     * createTime : 1504012204000
     * background : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg
     * readNum : 1
     * characterId : 2
     */

    private String updateTime;
    private int storyNum;
    private int topicId;
    private int spaceId;
    private String title;
    private String intro;
    private int status;
    private String createTime;
    private String background;
    private int readNum;
    private int characterId;
    private List<TagContentBean> tagMap;


    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getStoryNum() {
        return storyNum;
    }

    public void setStoryNum(int storyNum) {
        this.storyNum = storyNum;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
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

    public List<TagContentBean> getTagMap() {
        return tagMap;
    }

    public void setTagMap(List<TagContentBean> tagMap) {
        this.tagMap = tagMap;
    }
}
