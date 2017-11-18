package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/11/3.
 */

public class ResponseGroupMemberBean {
    /**
     * admin : true
     * blocked : false
     * characterId : 0
     * groupId : 25058636529665
     * hxUserName : sc2054380109
     * id : 6
     */

    private boolean admin;
    private boolean blocked;
    private int characterId;
    private String groupId;
    private String hxUserName;
    private int id;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getHxUserName() {
        return hxUserName;
    }

    public void setHxUserName(String hxUserName) {
        this.hxUserName = hxUserName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
