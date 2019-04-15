package com.shanchain.shandata.ui.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;

import cn.jpush.im.android.api.enums.ContentType;

/**
 * Created by WealChen
 * Date : 2019/4/3
 * Describe :
 */
@Table(name = "message", id = "_id")
public class MessageEntry extends Model {

    @Column(name = "Conversation")
    public ConversationEntry mConversationEntry;

    @Column(name = "FormUser")
    public UserEntry formUser;

    @Column(name = "MessageText")
    public String messageText;

    @Column(name = "TimeString")
    public long timeString;

    @Column(name = "MessageType")
    public ContentType messageType;

    @Column(name = "MediaFilePath")
    public String mediaFilePath;

    @Column(name = "Duration")
    public long duration;

    @Column(name = "Progress")
    public String progress;

    public MessageEntry() {
        super();
    }

    public MessageEntry(String messageText) {
        super();
        this.messageText = messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setTimeString(long timeString) {
        this.timeString = timeString;
    }

    public void setMessageType(ContentType messageType) {
        this.messageType = messageType;
    }

    public void setMediaFilePath(String mediaFilePath) {
        this.mediaFilePath = mediaFilePath;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setFormUser(UserEntry formUser) {
        this.formUser = formUser;
    }

    public void setConversationEntry(ConversationEntry conversationEntry) {
        mConversationEntry = conversationEntry;
    }

    public ConversationEntry getConversationEntry() {
        return mConversationEntry;
    }

    public UserEntry getFormUser() {
        return formUser;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getTimeString() {
        return timeString;
    }

    public ContentType getMessageType() {
        return messageType;
    }

    public String getMediaFilePath() {
        return mediaFilePath;
    }

    public long getDuration() {
        return duration;
    }

    public String getProgress() {
        return progress;
    }

    //根据有效期删除过期的历史消息
    public static void deleteMessage(int date) {
        long effectiveTime = date * 24 * 60 * 60 * 1000;
        final long time = System.currentTimeMillis() - effectiveTime;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                new Delete().from(MessageEntry.class).where("TimeString <= ? ", time).execute();
            }
        });
        thread.start();
    }
}
