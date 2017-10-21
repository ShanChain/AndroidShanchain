package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/21.
 */

public class RNTopicDetailExt {


    /**
     * gData : {"userId":"25","token":"695d42dc2420419ca892d37d462a6b941508576387064","spaceId":"16","characterId":"11"}
     * data : {"updateTime":"1508297253000","storyNum":18,"topicId":6,"spaceId":16,"tagMap":[],"title":"一起结婚吧","intro":"啦啦啦啦安拉啊","status":1,"createTime":"1504012204000","background":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","readNum":1,"characterId":2}
     */

    private RNTopicGDataBean gData;
    private RNTopicDataBean data;

    public RNTopicGDataBean getgData() {
        return gData;
    }

    public void setgData(RNTopicGDataBean gData) {
        this.gData = gData;
    }

    public RNTopicDataBean getData() {
        return data;
    }

    public void setData(RNTopicDataBean data) {
        this.data = data;
    }
}
