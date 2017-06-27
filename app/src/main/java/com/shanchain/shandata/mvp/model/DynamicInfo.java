package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/6/12.
 */

public class DynamicInfo {
    /** 描述：
     * 0   挑战
     * 1   故事
     * 2   每日描述
     * 3   每月描述
     * */
    private int type;
    private String challengeContent;
    private String getChallengeResult;
    private String storyContent;
    private String getStoryResult;
    private String time;
    private String left1;
    private String left2;
    private String right1;
    private String right2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChallengeContent() {
        return challengeContent;
    }

    public void setChallengeContent(String challengeContent) {
        this.challengeContent = challengeContent;
    }

    public String getGetChallengeResult() {
        return getChallengeResult;
    }

    public void setGetChallengeResult(String getChallengeResult) {
        this.getChallengeResult = getChallengeResult;
    }

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    public String getGetStoryResult() {
        return getStoryResult;
    }

    public void setGetStoryResult(String getStoryResult) {
        this.getStoryResult = getStoryResult;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLeft1() {
        return left1;
    }

    public void setLeft1(String left1) {
        this.left1 = left1;
    }

    public String getLeft2() {
        return left2;
    }

    public void setLeft2(String left2) {
        this.left2 = left2;
    }

    public String getRight1() {
        return right1;
    }

    public void setRight1(String right1) {
        this.right1 = right1;
    }

    public String getRight2() {
        return right2;
    }

    public void setRight2(String right2) {
        this.right2 = right2;
    }
}
