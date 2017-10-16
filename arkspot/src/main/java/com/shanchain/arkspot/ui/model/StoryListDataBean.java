package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/13.
 */

public class StoryListDataBean {


    /**
     * commentCount : 2
     * content : 第一个故事的第一个续集第二个故事
     * createTime : 1507959567000
     * genNum : 2
     * info : {"characterId":11,"createTime":1506048691000,"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg","intro":"楚汉之地，东方最神秘的地域之一。虔诚的大河子民中，他那样另类：既无视信仰，又热衷利益，更不择手段。","modelId":16,"modelNo":2,"name":"刘邦","userId":4}
     * intro :
     * lineNum : 1
     * locNum : 2
     * parentForkId : 0
     * rootId : 1
     * spaceId : 16
     * status : 1
     * storyId : 5
     * storyPics : []
     * supportCount : 1
     * tail : {"name":"时空大圣","rate":0,"tailId":12}
     * title :
     * topicSet : []
     * transpond : 1
     * type : 0
     * updateTime : 1507959567000
     */

    private int commentCount;
    private String content;
    private long createTime;
    private int genNum;
    private StoryInfoBean info;
    private String intro;
    private int lineNum;
    private int locNum;
    private int parentForkId;
    private int rootId;
    private int spaceId;
    private int status;
    private int storyId;
    private int supportCount;
    private TailBean tail;
    private String title;
    private int transpond;
    private int type;
    private long updateTime;
    private List<?> storyPics;
    private List<?> topicSet;

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

    public StoryInfoBean getInfo() {
        return info;
    }

    public void setInfo(StoryInfoBean info) {
        this.info = info;
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

    public TailBean getTail() {
        return tail;
    }

    public void setTail(TailBean tail) {
        this.tail = tail;
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

    public List<?> getStoryPics() {
        return storyPics;
    }

    public void setStoryPics(List<?> storyPics) {
        this.storyPics = storyPics;
    }

    public List<?> getTopicSet() {
        return topicSet;
    }

    public void setTopicSet(List<?> topicSet) {
        this.topicSet = topicSet;
    }
}
