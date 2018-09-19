package com.shanchain.shandata.ui.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhoujian on 2017/11/3.
 */

public class SceneDetailData {

    /**
     * groupId : 31749455282179
     * groupName : 亚雄的对话场景
     * spaceId : 140
     * iconUrl :
     * groupDesc : 亚雄创建的对话场景
     * maxUsers : 100
     * membersOnly : false
     * allowinvites : false
     * groupOwner : {"hxUserName":"sc-738726912","characterId":190,"userId":4,"hxPassword":"hx1509676446640"}
     * valid : true
     * createTime : 1509677041000
     * public : true
     */

    private String groupId;
    private String groupName;
    private int spaceId;
    private String iconUrl;
    private String groupDesc;
    private int maxUsers;
    private boolean membersOnly;
    private boolean allowinvites;
    private GroupOwnerBean groupOwner;
    private boolean valid;
    private long createTime;
    @SerializedName("public")
    private boolean publicX;

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

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
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

    public boolean isAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(boolean allowinvites) {
        this.allowinvites = allowinvites;
    }

    public GroupOwnerBean getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(GroupOwnerBean groupOwner) {
        this.groupOwner = groupOwner;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isPublicX() {
        return publicX;
    }

    public void setPublicX(boolean publicX) {
        this.publicX = publicX;
    }
}
