package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/11/2.
 */

public class ResponseFocusContactBean {

    /**
     * characterId : 10
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg
     * intro : 少年杨戬有一个秘密，谁也不知道的秘密。额头内埋藏着“眼”。从外表看不出来，但无时不能深刻感知到其中蕴含的力量。没有人告诉他这是什么，但肯定有别于正常的人类。
     * modelNo : 1
     * name : 少年杨戬有一个秘密，谁也不知道的秘密。额头内埋藏着“眼”。从外表看不出来，但无时不能深刻感知到其中蕴含的力量。没有人告诉他这是什么，但肯定有别于正常的人类。
     * userId : 6
     */

    private int characterId;
    private String headImg;
    private String intro;
    private int modelNo;
    private String name;
    private int userId;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
