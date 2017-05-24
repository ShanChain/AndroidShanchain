package com.shanchain.mvp.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/5/23.
 */

public class PublisherInfo {
    /** 描述：头像url*/
    private String avatarUrl;
    /** 描述：姓名*/
    private String name;
    /** 描述：时间*/
    private String time;

    /** 描述：发表的图片集合url*/
    private List<String> images;
    /** 描述：点赞数*/
    private int likes;
    /** 描述：评论数*/
    private int comments;

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
