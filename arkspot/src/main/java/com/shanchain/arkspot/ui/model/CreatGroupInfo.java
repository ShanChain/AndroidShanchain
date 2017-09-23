package com.shanchain.arkspot.ui.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/19.
 */

public class CreatGroupInfo {

    /**
     * approval : true
     * desc : server create group
     * groupname : testrestgrp12
     * maxusers : 300
     * members : ["sc2054380108","sc2054380109","20"]
     * owner : ddhfieio
     * public : true
     */

    private boolean approval;
    private String desc;
    private String groupname;
    private int maxusers;
    private String owner;
    @SerializedName("public")
    private boolean publicX;
    private List<String> members;

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isPublicX() {
        return publicX;
    }

    public void setPublicX(boolean publicX) {
        this.publicX = publicX;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
