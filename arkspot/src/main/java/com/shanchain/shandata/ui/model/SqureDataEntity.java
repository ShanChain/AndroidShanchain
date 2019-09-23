package com.shanchain.shandata.ui.model;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/7/26
 * Describe :
 */
public class SqureDataEntity implements Serializable , MultiItemEntity {
    int id;
    int userId;
    String nickName;
    String headIcon;
    String title;
    String content;
    int praiseCount;
    int reviceCount;
    int collectCount;
    long createTime;
    long updateTime;
    int isHot;
    int isRecommend;
    String listImg;
    String isPraise;
    String isAttention;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getReviceCount() {
        return reviceCount;
    }

    public void setReviceCount(int reviceCount) {
        this.reviceCount = reviceCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getListImg() {
        return listImg;
    }

    public void setListImg(String listImg) {
        this.listImg = listImg;
    }

    public String getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(String isPraise) {
        this.isPraise = isPraise;
    }

    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int getItemType() {
        if(TextUtils.isEmpty(getListImg())){
            return 0;
        }else {
            String []attr = getListImg().replaceAll("\\\\","").split(",");
            if(attr.length==1){
                return 1;
            }else {
                return 2;
            }
        }
    }
}
