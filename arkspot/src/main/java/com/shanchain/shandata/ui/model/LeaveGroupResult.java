package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/11/20.
 */

public class LeaveGroupResult {
    /**
     * action : remove_member
     * groupid : 25058636529665
     * result : true
     * user : sc2054380133
     */

    private String action;
    private String groupid;
    private boolean result;
    private String user;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
