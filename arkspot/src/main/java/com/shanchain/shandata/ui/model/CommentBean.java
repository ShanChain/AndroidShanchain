package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/23.
 */

public class CommentBean {
    /**
     * commentId : 13
     * storyId : 8
     * characterId : 68
     * content : 蝴蝶如我，我如蝴蝶
     * supportCount : 0
     * createTime : 1507639682000
     * isAnon : 0
     * userId : null
     * mySupport : false
     */

    private int commentId;
    private int storyId;
    private int characterId;
    private String content;
    private int supportCount;
    private long createTime;
    private int isAnon;
    private int userId;
    private boolean mySupport;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIsAnon() {
        return isAnon;
    }

    public void setIsAnon(int isAnon) {
        this.isAnon = isAnon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isMySupport() {
        return mySupport;
    }

    public void setMySupport(boolean mySupport) {
        this.mySupport = mySupport;
    }
}
