package com.shanchain.shandata.ui.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import cn.jpush.im.android.api.enums.ContentType;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by WealChen
 * Date : 2019/4/3
 * Describe :
 */
@Entity
public class MessageEntry {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String roomId;
    @Unique
    private String msgId;
    private Long userId;
    private String userName;
    @NotNull
    private String avatar;
    @NotNull
    private String jgUserName;
    private String displayName;
    private String messageText;
    private long timeString;
    private String messageType;
    private String fileFormat;
    private String mediaFilePath;
    private long duration;
    private String progress;
    @Generated(hash = 1084024613)
    public MessageEntry(Long id, @NotNull String roomId, String msgId, Long userId,
            String userName, @NotNull String avatar, @NotNull String jgUserName,
            String displayName, String messageText, long timeString,
            String messageType, String fileFormat, String mediaFilePath,
            long duration, String progress) {
        this.id = id;
        this.roomId = roomId;
        this.msgId = msgId;
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
        this.jgUserName = jgUserName;
        this.displayName = displayName;
        this.messageText = messageText;
        this.timeString = timeString;
        this.messageType = messageType;
        this.fileFormat = fileFormat;
        this.mediaFilePath = mediaFilePath;
        this.duration = duration;
        this.progress = progress;
    }
    @Generated(hash = 1450281958)
    public MessageEntry() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRoomId() {
        return this.roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getMsgId() {
        return this.msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getJgUserName() {
        return this.jgUserName;
    }
    public void setJgUserName(String jgUserName) {
        this.jgUserName = jgUserName;
    }
    public String getDisplayName() {
        return this.displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getMessageText() {
        return this.messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    public long getTimeString() {
        return this.timeString;
    }
    public void setTimeString(long timeString) {
        this.timeString = timeString;
    }
    public String getMessageType() {
        return this.messageType;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public String getFileFormat() {
        return this.fileFormat;
    }
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
    public String getMediaFilePath() {
        return this.mediaFilePath;
    }
    public void setMediaFilePath(String mediaFilePath) {
        this.mediaFilePath = mediaFilePath;
    }
    public long getDuration() {
        return this.duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    public String getProgress() {
        return this.progress;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }


}
