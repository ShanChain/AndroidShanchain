package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/28.
 */

public class ResponseHxUerBean {

    /**
     * characterId : 93
     * hxUserName : 20
     */

    private int characterId;
    private String hxUserName;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public String getHxUserName() {
        return hxUserName;
    }

    public void setHxUserName(String hxUserName) {
        this.hxUserName = hxUserName;
    }

    @Override
    public String toString() {
        return "ResponseHxUerBean{" +
                "characterId=" + characterId +
                ", hxUserName='" + hxUserName + '\'' +
                '}';
    }
}
