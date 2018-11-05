package cn.jiguang.imui.model;

import java.util.HashMap;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;
import cn.jiguang.imui.model.MyMessage;

public class ChatEventMessage extends MyMessage implements IMessage, com.chad.library.adapter.base.entity.MultiItemEntity {

    public static final int type1 = 1;  //为领取
    public static final int type2 = 2;  //已领取

    //赏金
    private String bounty;
    //任务人角色id
    private int characterId;
    private IUser iUser;

    private int commentCount;
    private int createTime;
    //任务内容
    private String intro;
    //截至时间
    private String limitedTime;
    //领取人数
    private int receiveCount;
    //任务领取状态
    private int receiveStatus;
    //广场id
    private String squareId;
    //点赞人数
    private int supportCount;
    //任务ID
    private int taskId;
    //是否置顶
    private int topping;
    //任务人的用户Id
    private int userId;
    //是否确认完成
    private int verify;

    public ChatEventMessage(String text, int type) {
        super(text, type);
    }

    public IUser getiUser() {
        return iUser;
    }

    public void setiUser(IUser iUser) {
        this.iUser = iUser;
    }
    public String getBounty() {
        return bounty;
    }

    public void setBounty(String bounty) {
        this.bounty = bounty;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(String limitedTime) {
        this.limitedTime = limitedTime;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public int getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(int receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getSquareId() {
        return squareId;
    }

    public void setSquareId(String squareId) {
        this.squareId = squareId;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTopping() {
        return topping;
    }

    public void setTopping(int topping) {
        this.topping = topping;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }


    @Override
    public String getMsgId() {
        return null;
    }

    @Override
    public IUser getFromUser() {
        return null;
    }

    @Override
    public String getTimeString() {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public MessageStatus getMessageStatus() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public String getMediaFilePath() {
        return null;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public String getProgress() {
        return null;
    }

    @Override
    public HashMap<String, String> getExtras() {
        return null;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
