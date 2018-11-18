package cn.jiguang.imui.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.HashMap;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;

public class ChatEventMessage extends MyMessage implements IMessage, MultiItemEntity,Serializable {

    public static final int type1 = 0;  //未领取
    public static final int type2 = 1;  //已领取
    public static final int type3 = 2;  //已完成
    public static final int type4 = 3;  //已过期
    public static final int type5 = 4;  //查看任务
    /**
     * taskId : 44
     * verify : 0
     * bounty : 3.5
     * topping : 0
     * name=1078585
     * headImg=http://shanchain-picture.oss-cn-beijing.aliyuncs.com/default_%20head_img.png
     * intro : {"content":"嘿","imgs":[],"spanBeanList":[]}
     * roomId : 12766207
     * roomName=20.04318544,110.31167518
     * statusHistory : ["已发布","已领取","任务取消","已重新发布"]
     * createTime : 1541664580000
     * expiryTime : 1543119840000
     * receiveTime : 1542268818000
     * status : 5
     * receiveCount : 0
     * commentCount : 0
     * supportCount : 0
     * characterId : 95
     * verifyTime : null
     * unfinishedTime : null
     * currency : rmb
     * releaseHash : 0xwz655IknfYCO
     * lastHash : 0xzhrxVj7lUxkw
     * userId : 3
     */

    private String taskId;
    private int verify;
    private String bounty;
    private String bountyString;
    private int topping;
    private String name;
    private String headImg;
    private String intro;
    private String roomId;
    private String roomName;
    private String statusHistory;
    private long createTime;
    private long expiryTime;
    private long receiveTime;
    private int status;
    private int receiveCount;
    private int commentCount;
    private int supportCount;
    private int characterId;
    private Object verifyTime;
    private Object unfinishedTime;
    private String currency;
    private String releaseHash;
    private String lastHash;
    private int userId;
    private int itemType;


    private IUser iUser;

    public ChatEventMessage() {
    }

    public ChatEventMessage(String text, int type) {
        super(text, type);
    }

    public String getBountyString() {
        return bountyString;
    }

    public void setBountyString(String bountyString) {
        this.bountyString = bountyString;
    }

    public IUser getiUser() {
        return iUser;
    }

    public void setiUser(IUser iUser) {
        this.iUser = iUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Override
    public String getProgress() {
        return null;
    }

    @Override
    public HashMap<String, String> getExtras() {
        return null;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public String getBounty() {
        return bounty;
    }

    public void setBounty(String bounty) {
        this.bounty = bounty;
    }

    public int getTopping() {
        return topping;
    }

    public void setTopping(int topping) {
        this.topping = topping;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(String statusHistory) {
        this.statusHistory = statusHistory;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public Object getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Object verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Object getUnfinishedTime() {
        return unfinishedTime;
    }

    public void setUnfinishedTime(Object unfinishedTime) {
        this.unfinishedTime = unfinishedTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReleaseHash() {
        return releaseHash;
    }

    public void setReleaseHash(String releaseHash) {
        this.releaseHash = releaseHash;
    }

    public String getLastHash() {
        return lastHash;
    }

    public void setLastHash(String lastHash) {
        this.lastHash = lastHash;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
