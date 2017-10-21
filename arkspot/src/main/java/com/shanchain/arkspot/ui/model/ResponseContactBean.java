package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/21.
 */

public class ResponseContactBean {

    /**
     * headImg : http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg
     * modelId : 13
     * name : 程咬金
     */

    private String headImg;
    private int modelId;
    private String name;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
