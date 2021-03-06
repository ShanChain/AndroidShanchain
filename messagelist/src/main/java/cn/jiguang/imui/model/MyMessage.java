package cn.jiguang.imui.model;

import java.util.HashMap;
import java.util.UUID;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;


public class MyMessage implements IMessage {

    private long id;
    private String text;
    private String timeString;
    private int type;
    private String hxUserName;
    private IUser user;
    private DefaultUser defaultUser;
    private String mediaFilePath;
    private long duration;
    private String progress;
    private ChatEventMessage chatEventMessage;
    //    private MessageStatus mMsgStatus = MessageStatus.SEND_GOING;
    private MessageStatus mMsgStatus = MessageStatus.CREATED;

    public MyMessage() {
        this.text = text;
        this.type = type;
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    public MyMessage(int type) {
        this.text = text;
        this.type = type;
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    public MyMessage(String text, int type) {
        this.text = text;
        this.type = type;
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    @Override
    public String getMsgId() {
        return String.valueOf(id);
    }

    public long getId() {
        return this.id;
    }


    public void setText(String text) {
        this.text = text;
    }

    public ChatEventMessage getChatEventMessage() {
        return chatEventMessage;
    }

    public void setChatEventMessage(ChatEventMessage chatEventMessage) {
        this.chatEventMessage = chatEventMessage;
    }

    @Override
    public IUser getFromUser() {
        if (defaultUser == null) {
            return new DefaultUser(0, "user1", null);
        }
        return defaultUser;
    }

    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public void setUserInfo(DefaultUser user) {

        this.defaultUser = user;
    }

    public void setMediaFilePath(String path) {
        this.mediaFilePath = path;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String getProgress() {
        return progress;
    }

    @Override
    public HashMap<String, String> getExtras() {
        return null;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Override
    public String getTimeString() {
        return timeString;
    }

    public void setType(int type) {
        if (type >= 0 && type <= 12) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Message type should not take the value between 0 and 12");
        }


    }

    @Override
    public int getType() {
        return type;
    }

    /**
     * Set Message status. After sending Message, change the status so that the progress bar will dismiss.
     *
     * @param messageStatus {@link cn.jiguang.imui.commons.models.IMessage.MessageStatus}
     */

    public void setMessageStatus(MessageStatus messageStatus) {
        this.mMsgStatus = messageStatus;
    }

    @Override
    public MessageStatus getMessageStatus() {
        return mMsgStatus;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getMediaFilePath() {
        return mediaFilePath;
    }
}