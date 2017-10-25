package com.shanchain.arkspot.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/10/23.
 */

public class ResponseCharacterBrief implements Serializable{

    /**
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg
     * intro : 出自手游《王者荣耀》，作为一名炼金师，太乙真人大部分时间都宅在家里研究各种稀奇古怪的东西，现在是时候把这些发明拿到战场上试验一下威力了~
     * name : 太乙真人
     * modelNo : 1
     * characterId : 9
     * userId : 4
     */

    private String headImg;
    private String intro;
    private String name;
    private int modelNo;
    private int characterId;
    private int userId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getModelNo() {
        return modelNo;
    }

    public void setModelNo(int modelNo) {
        this.modelNo = modelNo;
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

    @Override
    public String toString() {
        return "ResponseCharacterBrief{" +
                "headImg='" + headImg + '\'' +
                ", intro='" + intro + '\'' +
                ", name='" + name + '\'' +
                ", modelNo=" + modelNo +
                ", characterId=" + characterId +
                ", userId=" + userId +
                '}';
    }
}
