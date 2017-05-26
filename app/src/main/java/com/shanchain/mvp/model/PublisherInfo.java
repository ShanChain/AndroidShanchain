package com.shanchain.mvp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhoujian on 2017/5/23.
 */

public class PublisherInfo implements Serializable{
    /** 描述：头像url*/
    private String avatarUrl;
    /** 描述：姓名*/
    private String name;
    /** 描述：时间*/
    private String time;
    /** 描述：动态描述*/
    private String des;

    /** 描述：发表的图片集合url*/
    private List<String> images;
    /** 描述：点赞数*/
    private int likes;
    /** 描述：评论数*/
    private int comments;
    /** 描述：条目类型:
     * 1 普通类型
     * 2 挑战类型
     * 3 故事类型
     * */
    private int type;

    private String stroyImgUrl;

    private String iconUrl;

    private  String title;

    private  String challegeTime;

    private  String addr;

    private String activeDes;

    private String otherDes;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getStroyImgUrl() {
        return stroyImgUrl;
    }

    public void setStroyImgUrl(String stroyImgUrl) {
        this.stroyImgUrl = stroyImgUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChallegeTime() {
        return challegeTime;
    }

    public void setChallegeTime(String challegeTime) {
        this.challegeTime = challegeTime;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getActiveDes() {
        return activeDes;
    }

    public void setActiveDes(String activeDes) {
        this.activeDes = activeDes;
    }

    public String getOtherDes() {
        return otherDes;
    }

    public void setOtherDes(String otherDes) {
        this.otherDes = otherDes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    
    
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
