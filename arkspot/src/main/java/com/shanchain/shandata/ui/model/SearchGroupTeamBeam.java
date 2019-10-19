package com.shanchain.shandata.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WealChen
 * Date : 2019/8/20
 * Describe :
 */
public class SearchGroupTeamBeam implements Serializable {
    String id;
    String name;
    String roomId;
    String roomName;
    String createUser;
    String inviteCode;
    List<SearchTeamBean> tDiggingJoinLogs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<SearchTeamBean> gettDiggingJoinLogs() {
        return tDiggingJoinLogs;
    }

    public void settDiggingJoinLogs(List<SearchTeamBean> tDiggingJoinLogs) {
        this.tDiggingJoinLogs = tDiggingJoinLogs;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
