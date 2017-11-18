package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/9/19.
 */

public class ModifyGroupInfo {

    /**
     * maxusers : 100
     * description : test group for dev
     * groupname : first_group
     */

    private String maxusers;
    private String description;
    private String groupname;

    public String getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(String maxusers) {
        this.maxusers = maxusers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
