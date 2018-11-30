package com.shanchain.shandata.ui.model;

public class Members {
    /**
     * id : 47
     * roomId : 15237570
     * hxUserName : 11111
     * characterId : 11111
     * userId : 11111
     * spaceId : 20.04318544,110.32316269
     */

    private int id;
    private String roomId;
    private String hxUserName;
    private int characterId;
    private int userId;
    private String spaceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHxUserName() {
        return hxUserName;
    }

    public void setHxUserName(String hxUserName) {
        this.hxUserName = hxUserName;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }
}
