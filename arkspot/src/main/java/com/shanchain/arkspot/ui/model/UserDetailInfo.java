package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/9/27.
 */

public class UserDetailInfo {

    /**
     * aliasName : 1
     * characterId : 7
     * createTime : 1506030760000
     * disc : 1
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/40e226e4a2244dabb1d9e4bab19fd5ec.jpg
     * intro : 楚汉之地，东方最神秘的地域之一。虔诚的大河子民中，他那样另类：既无视信仰，又热衷利益，更不择手段。
     * modelId : 16
     * modelNo : 1
     * name : 刘邦
     * spaceId : 16
     * userInfo : {"birthday":"2017-09-21","country":1,"created":1506094774000,"email":1,"headIcon":1,"idNumber":"000000000000000000","level":99,"loggedIn":1506094774000,"mobile":"80082088201","nickName":"管理员1号","sex":1,"title":"管理好我的魂穿","userId":1,"userName":"管理员1号"}
     */

    private int aliasName;
    private int characterId;
    private long createTime;
    private int disc;
    private String headImg;
    private String intro;
    private int modelId;
    private int modelNo;
    private String name;
    private int spaceId;
    private UserInfoBean userInfo;

    public int getAliasName() {
        return aliasName;
    }

    public void setAliasName(int aliasName) {
        this.aliasName = aliasName;
    }

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

    public int getDisc() {
        return disc;
    }

    public void setDisc(int disc) {
        this.disc = disc;
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

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }
}
