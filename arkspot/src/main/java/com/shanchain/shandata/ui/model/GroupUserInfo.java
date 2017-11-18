package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/9/28.
 */

public class GroupUserInfo implements Serializable{


    /**
     * characterId : 7
     * createTime : 1506030760000
     * headImg : 1
     * intro : 楚汉之地，东方最神秘的地域之一。虔诚的大河子民中，他那样另类：既无视信仰，又热衷利益，更不择手段。
     * modelId : 16
     * modelNo : 1
     * name : 刘邦
     * spaceId : 16
     * userInfo : {"birthday":"2017-09-21","created":1506094774000,"idNumber":"000000000000000000","level":99,"loggedIn":1506094774000,"mobile":"80082088201","nickName":"管理员1号","sex":1,"title":"管理好我的魂穿","userId":1,"userName":"管理员1号"}
     */

    private int characterId;
    private long createTime;
    private String headImg;
    private String intro;
    private int modelId;
    private int modelNo;
    private String name;
    private int spaceId;
    private GroupUserBasicInfo userInfo;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getModelNo() {
        return modelNo;
    }

    public void setModelNo(int modelNo) {
        this.modelNo = modelNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public GroupUserBasicInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(GroupUserBasicInfo userInfo) {
        this.userInfo = userInfo;
    }
}
