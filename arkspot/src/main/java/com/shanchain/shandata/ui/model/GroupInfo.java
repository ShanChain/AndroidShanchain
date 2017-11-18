package com.shanchain.shandata.ui.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhoujian on 2017/10/19.
 */

public class GroupInfo {

    /**
     * allowinvites : true
     * createTime : 1503415128000
     * groupDesc : 3231
     * groupId : 25057176911873
     * groupName : 1111
     * groupOwner : {"characterId":77,"hxPassword":"hx1503276332216","hxUserName":"sc2054380108"}
     * iconUrl : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/828fcb3fa0674e5cbd3dc1393af90191.jpg?x-oss-process=image/resize,m_fixed,h_1100,w_733
     * maxUsers : 123
     * membersOnly : false
     * public : true
     * valid : false
     */

    private boolean allowinvites;
    private long createTime;
    private String groupDesc;
    private String groupId;
    private String groupName;
    private GroupOwnerInfo groupOwner;
    private String iconUrl;
    private int maxUsers;
    private boolean membersOnly;
    @SerializedName("public")
    private boolean publicX;
    private boolean valid;

    public boolean isAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(boolean allowinvites) {
        this.allowinvites = allowinvites;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupOwnerInfo getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(GroupOwnerInfo groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

    public boolean isPublicX() {
        return publicX;
    }

    public void setPublicX(boolean publicX) {
        this.publicX = publicX;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "allowinvites=" + allowinvites +
                ", createTime=" + createTime +
                ", groupDesc='" + groupDesc + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupOwner=" + groupOwner +
                ", iconUrl='" + iconUrl + '\'' +
                ", maxUsers=" + maxUsers +
                ", membersOnly=" + membersOnly +
                ", publicX=" + publicX +
                ", valid=" + valid +
                '}';
    }
}
