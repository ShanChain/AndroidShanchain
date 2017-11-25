package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class StoryModelBean implements Serializable{

    /**
     * background :
     * beFav : true
     * characterId : 7
     * characterImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg
     * characterName : 刘邦
     * commendCount : 0
     * createTime : 1508762821000
     * detailId : s1
     * genNum : 1
     * intro : add some pic
     * lineNum : 1
     * rootId : 1
     * spaceId : 16
     * status : 1
     * supportCount : 0
     * title : picstory
     * transpond : 3
     * type : 1
     */

    private String background;
    private boolean beFav;
    private int characterId;
    private String characterImg;
    private String characterName;
    private int commendCount;
    private long createTime;
    private String detailId;
    private int genNum;
    private String intro;
    private int lineNum;
    private int rootId;
    private int spaceId;
    private int status;
    private int supportCount;
    private String title;
    private int transpond;
    private int type;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public boolean isBeFav() {
        return beFav;
    }

    public void setBeFav(boolean beFav) {
        this.beFav = beFav;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public String getCharacterImg() {
        return characterImg;
    }

    public void setCharacterImg(String characterImg) {
        this.characterImg = characterImg;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public int getCommendCount() {
        return commendCount;
    }

    public void setCommendCount(int commendCount) {
        this.commendCount = commendCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
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

    public NovelModel getNovelMovel(){
        NovelModel novelModel = new NovelModel();
        novelModel.setCharacterId(this.characterId);
        novelModel.setStoryId(Integer.parseInt(this.detailId.substring(1)));
        novelModel.setCommendCount(this.commendCount);
        novelModel.setIntro(this.intro);
        novelModel.setCreateTime(this.createTime);
        novelModel.setRootId(this.rootId);
        novelModel.setSpaceId(this.spaceId);
        novelModel.setTitle(this.title);
        novelModel.setGenNum(this.genNum);
        novelModel.setLineNum(lineNum);
        novelModel.setStatus(status);
        novelModel.setSupportCount(supportCount);
        novelModel.setType(type);
        novelModel.setTranspond(transpond);
        return novelModel;
    }

    public DynamicModel getDynamicModel(){
        DynamicModel dynamicModel = new DynamicModel();
        dynamicModel.setCharacterId(this.characterId);
        dynamicModel.setStoryId(Integer.parseInt(this.detailId.substring(1)));
        dynamicModel.setCommendCount(this.commendCount);
        dynamicModel.setIntro(this.intro);
        dynamicModel.setCreateTime(this.createTime);
        dynamicModel.setRootId(this.rootId);
        dynamicModel.setSpaceId(this.spaceId);
        dynamicModel.setGenNum(this.genNum);
        dynamicModel.setLineNum(lineNum);
        dynamicModel.setStatus(status);
        dynamicModel.setSupportCount(supportCount);
        dynamicModel.setType(type);
        dynamicModel.setTranspond(transpond);
        return dynamicModel;
    }

    public TopicModel getTopicModel(){
        TopicModel topicModel = new TopicModel();
        topicModel.setCharacterId(this.characterId);
        topicModel.setBackground(background);
        topicModel.setIntro(this.intro);
        topicModel.setCreateTime(this.createTime);
        topicModel.setSpaceId(this.spaceId);
        topicModel.setStatus(status);
        topicModel.setTopicId(Integer.parseInt(detailId.substring(1)));
        return topicModel;
    }

    public CharacterInfo getCharacterInfo(){
        CharacterInfo characterInfo = new CharacterInfo();
        characterInfo.setCharacterId(this.characterId);
        characterInfo.setHeadImg(characterImg);
        characterInfo.setName(characterName);
        characterInfo.setSpaceId(spaceId);
        characterInfo.setIntro(intro);
        return characterInfo;
    }

    @Override
    public String toString() {
        return "StoryModelBean{" +
                "background='" + background + '\'' +
                ", beFav=" + beFav +
                ", characterId=" + characterId +
                ", characterImg='" + characterImg + '\'' +
                ", characterName='" + characterName + '\'' +
                ", commendCount=" + commendCount +
                ", createTime=" + createTime +
                ", detailId='" + detailId + '\'' +
                ", genNum=" + genNum +
                ", intro='" + intro + '\'' +
                ", lineNum=" + lineNum +
                ", rootId=" + rootId +
                ", spaceId=" + spaceId +
                ", status=" + status +
                ", supportCount=" + supportCount +
                ", title='" + title + '\'' +
                ", transpond=" + transpond +
                ", type=" + type +
                '}';
    }
}
