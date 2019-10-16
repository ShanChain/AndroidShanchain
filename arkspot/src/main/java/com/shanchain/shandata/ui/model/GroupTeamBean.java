package com.shanchain.shandata.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by WealChen
 * Date : 2019/8/8
 * Describe :
 */
public class GroupTeamBean implements Serializable {
    int id;
    String name;
    String roomId;
    String roomName;
    int userCount;
    int spaceId;
    String inviteCode;
    String status;
    String level;
    String roomImage;
    String createUser;
    String createTime;
//    String tDiggingJoinLogs;
    List<TDiggingJoinLogs> tDiggingJoinLogs;


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public List<TDiggingJoinLogs> gettDiggingJoinLogs() {
        return tDiggingJoinLogs;
    }

    public void settDiggingJoinLogs(List<TDiggingJoinLogs> tDiggingJoinLogs) {
        this.tDiggingJoinLogs = tDiggingJoinLogs;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "GroupTeamBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", userCount=" + userCount +
                ", spaceId=" + spaceId +
                ", inviteCode='" + inviteCode + '\'' +
                ", status='" + status + '\'' +
                ", level='" + level + '\'' +
                ", roomImage='" + roomImage + '\'' +
                ", createUser='" + createUser + '\'' +
                ", tDiggingJoinLogs=" + tDiggingJoinLogs +
                '}';
    }
}
