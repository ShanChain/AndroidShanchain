package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by WealChen
 * Date : 2019/7/30
 * Describe :
 */
public class CommentEntity implements Serializable {
    int id;
    String content;
    long createTime;
    int invitationId;
    int sendUserId;
    int toUserId;
    int parentId;
    int replyId;
    long updateTime;
    String sendNickName;
    String sendHeadIcon;
    String toNickName;
    String isAttention;
    int reviceCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(int invitationId) {
        this.invitationId = invitationId;
    }

    public int getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(int sendUserId) {
        this.sendUserId = sendUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getSendHeadIcon() {
        return sendHeadIcon;
    }

    public void setSendHeadIcon(String sendHeadIcon) {
        this.sendHeadIcon = sendHeadIcon;
    }

    public String getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getToNickName() {
        return toNickName;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }

    public int getReviceCount() {
        return reviceCount;
    }

    public void setReviceCount(int reviceCount) {
        this.reviceCount = reviceCount;
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", invitationId=" + invitationId +
                ", sendUserId=" + sendUserId +
                ", toUserId=" + toUserId +
                ", parentId=" + parentId +
                ", replyId=" + replyId +
                ", updateTime=" + updateTime +
                ", sendNickName='" + sendNickName + '\'' +
                ", sendHeadIcon='" + sendHeadIcon + '\'' +
                ", toNickName='" + toNickName + '\'' +
                ", isAttention='" + isAttention + '\'' +
                '}';
    }
}
