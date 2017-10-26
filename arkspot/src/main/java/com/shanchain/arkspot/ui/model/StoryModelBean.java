package com.shanchain.arkspot.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class StoryModelBean implements Serializable{

    /**
     * characterImg :
     * img : http://www.baidu.com
     * supportCount : 2
     * rootId : 0
     * detailId : t2
     * title : 送给三次元的慰问
     * type : 3
     * isFav : 0
     * spaceId : 16
     * tailId : 0
     * commendCount : 1
     * genNum : 0
     * tailName :
     * createTime : 1501636665000
     * intro : 今天让我们隔空向三次元送去慰问，让大叔大妈们更加了解我们。让我们荡起双桨，把三次元征服。今天让我们隔空向三次元送去慰问……
     * lineNum : 0
     * characterId : 2
     * status : 1
     * transpond : 0
     */

    private String characterImg;
    private String img;
    private int supportCount;
    private int rootId;
    private String detailId;
    private String title;
    private int type;
    private int isFav;
    private int spaceId;
    private int tailId;
    private int commendCount;
    private int genNum;
    private String tailName;
    private long createTime;
    private String intro;
    private int lineNum;
    private int characterId;
    private int status;
    private int transpond;


    public String getCharacterImg() {
        return characterImg;
    }

    public void setCharacterImg(String characterImg) {
        this.characterImg = characterImg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public int getTailId() {
        return tailId;
    }

    public void setTailId(int tailId) {
        this.tailId = tailId;
    }

    public int getCommendCount() {
        return commendCount;
    }

    public void setCommendCount(int commendCount) {
        this.commendCount = commendCount;
    }

    public int getGenNum() {
        return genNum;
    }

    public void setGenNum(int genNum) {
        this.genNum = genNum;
    }

    public String getTailName() {
        return tailName;
    }

    public void setTailName(String tailName) {
        this.tailName = tailName;
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

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
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

    public int getTranspond() {
        return transpond;
    }

    public void setTranspond(int transpond) {
        this.transpond = transpond;
    }

    @Override
    public String toString() {
        return "StoryModelBean{" +
                "characterImg='" + characterImg + '\'' +
                ", img='" + img + '\'' +
                ", supportCount=" + supportCount +
                ", rootId=" + rootId +
                ", detailId='" + detailId + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", isFav=" + isFav +
                ", spaceId=" + spaceId +
                ", tailId=" + tailId +
                ", commendCount=" + commendCount +
                ", genNum=" + genNum +
                ", tailName='" + tailName + '\'' +
                ", createTime=" + createTime +
                ", intro='" + intro + '\'' +
                ", lineNum=" + lineNum +
                ", characterId=" + characterId +
                ", status=" + status +
                ", transpond=" + transpond +
                '}';
    }
}
