package com.shanchain.shandata.ui.model;

public class Members {


    /**
     * username : 154578986558100163e0aa658102470
     * flag : 0
     * room_ctime : 2019-01-15 16:44:47
     * mtime : 2019-01-15 16:44:34
     * ctime : 2018-12-26 10:04:25
     */

    private String username;
    private int flag = 0;
    private String room_ctime;
    private String mtime;
    private String ctime;
    private boolean isSelect;
    private boolean isEdite;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRoom_ctime() {
        return room_ctime;
    }

    public void setRoom_ctime(String room_ctime) {
        this.room_ctime = room_ctime;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isEdite() {
        return isEdite;
    }

    public void setEdite(boolean edite) {
        isEdite = edite;
    }
}
