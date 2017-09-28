package com.shanchain.arkspot.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/9/22.
 */

public class GroupMemberBean implements Serializable{

    /**
     * admin : false
     * blocked : false
     * key : {"groupId":"27697479745538","hua":{"characterId":1222,"headImg":"default img","password":"hx1505802159842","title":"default title","userName":"sc-1425704407"}}
     */

    private boolean admin;
    private boolean blocked;
    private GroupKeyBenInfo key;

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

    public GroupKeyBenInfo getKey() {
        return key;
    }

    public void setKey(GroupKeyBenInfo key) {
        this.key = key;
    }
}
