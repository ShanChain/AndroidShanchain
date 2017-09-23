package com.shanchain.arkspot.ui.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/22.
 */

public class GroupInfo {
    /**
     * groupId : 27697479745538
     * groupName : qunmingcheng
     * groupDesc : hahahaha
     * maxUsers : 300
     * membersOnly : false
     * allowinvites : false
     * owner : sc-738727063
     * valid : false
     * createTime : 1505812775000
     * members : [{"key":{"username":"sc-738727190"},"blocked":false,"admin":false},{"key":{"username":"sc-738727191"},"blocked":false,"admin":false},{"key":{"username":"sc-1425704407"},"blocked":false,"admin":false}]
     * public : true
     */

    public String groupId;
    public String groupName;
    public String groupDesc;
    public int maxUsers;
    public boolean membersOnly;
    public boolean allowinvites;
    public String owner;
    public boolean valid;
    public long createTime;
    @SerializedName("public")
    public boolean publicX;
    public List<MembersBean> members;

    public static class MembersBean {

        public KeyBean key;
        public boolean blocked;
        public boolean admin;

        public static class KeyBean {
            /**
             * username : sc-738727190
             */

            public String username;
        }
    }
}
