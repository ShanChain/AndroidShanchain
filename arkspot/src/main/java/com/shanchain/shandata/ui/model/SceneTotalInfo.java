package com.shanchain.shandata.ui.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/26.
 */

public class SceneTotalInfo {
    /**
     * allowinvites : false
     * createTime : 1505859575000
     * groupDesc : hahahaha
     * groupId : 27697479745538
     * groupName : qunmingcheng
     * maxUsers : 300
     * members : [{"admin":false,"blocked":false,"key":{"groupId":"27697479745538","hua":{"characterId":1222,"headImg":"default img","password":"hx1505802159842","title":"default title","userName":"sc-1425704407"}}},{"admin":false,"blocked":false,"key":{"groupId":"27697479745538","hua":{"characterId":100,"headImg":"default img","password":"hx1505802185953","title":"default title","userName":"sc-738727191"}}},{"admin":false,"blocked":false,"key":{"groupId":"27697479745538","hua":{"characterId":101,"headImg":"default img","password":"hx1505802705192","title":"default title","userName":"sc-738727190"}}}]
     * membersOnly : false
     * owner : sc-738727063
     * public : true
     * valid : false
     */

    private boolean allowinvites;
    private long createTime;
    private String groupDesc;
    private String groupId;
    private String groupName;
    private int maxUsers;
    private ComUserInfo groupOwner;
    private boolean membersOnly;
    @SerializedName("public")
    private boolean publicX;
    private boolean valid;
    private List<GroupMemberBean> members;


    public ComUserInfo getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(ComUserInfo groupOwner) {
        this.groupOwner = groupOwner;
    }

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

    public List<GroupMemberBean> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberBean> members) {
        this.members = members;
    }
}
