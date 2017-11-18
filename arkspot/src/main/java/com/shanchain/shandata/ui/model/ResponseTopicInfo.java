package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/26.
 */

public class ResponseTopicInfo {


    /**
     * code : 000000
     * message : ok
     * data : {"content":[{"topicId":10,"spaceId":16,"title":"222","intro":"34242","background":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","storyNum":0,"readNum":0,"characterId":9,"status":0,"createTime":1508901526000,"updateTime":1508924242000,"tagMap":[{"tagId":19,"tagName":"龙珠","rate":0}]},{"topicId":12,"spaceId":16,"title":"22222233332222","intro":"324242","background":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","storyNum":0,"readNum":0,"characterId":13,"status":0,"createTime":1508482198000,"updateTime":1508924243000,"tagMap":[{"tagId":20,"tagName":"海贼王","rate":0},{"tagId":19,"tagName":"龙珠","rate":0}]},{"topicId":11,"spaceId":16,"title":"22222","intro":"4324234","background":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","storyNum":0,"readNum":0,"characterId":11,"status":1,"createTime":1508482131000,"updateTime":1508924244000,"tagMap":[]},{"topicId":8,"spaceId":16,"title":"三国群英","intro":"4324324","background":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","storyNum":4,"readNum":1,"characterId":9,"status":1,"createTime":1508480509000,"updateTime":1508984273000,"tagMap":[{"tagId":20,"tagName":"海贼王","rate":0},{"tagId":18,"tagName":"霸道总裁","rate":0},{"tagId":19,"tagName":"龙珠","rate":0}]},{"topicId":9,"spaceId":16,"title":"","intro":"234324234","background":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","storyNum":0,"readNum":0,"characterId":9,"status":-1,"createTime":1508469297000,"updateTime":1508982568000,"tagMap":[{"tagId":20,"tagName":"海贼王","rate":0},{"tagId":19,"tagName":"龙珠","rate":0}]},{"topicId":6,"spaceId":16,"title":"一起结婚吧","intro":"啦啦啦啦安拉啊","background":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","storyNum":18,"readNum":1,"characterId":2,"status":1,"createTime":1504012204000,"updateTime":1508982646000,"tagMap":[]},{"topicId":2,"spaceId":16,"title":"送给三次元的慰问","intro":"今天让我们隔空向三次元送去慰问，让大叔大妈们更加了解我们。让我们荡起双桨，把三次元征服。今天让我们隔空向三次元送去慰问\u2026\u2026","background":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","storyNum":30,"readNum":2,"characterId":2,"status":1,"createTime":1501636665000,"updateTime":1508297252000,"tagMap":[{"tagId":20,"tagName":"海贼王","rate":0}]}],"totalPages":1,"totalElements":7,"last":true,"numberOfElements":7,"first":true,"sort":[{"direction":"DESC","property":"createTime","ignoreCase":false,"nullHandling":"NATIVE","ascending":false,"descending":true}],"size":10,"number":0}
     */

    private String code;
    private String message;
    private ResponseTopicData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseTopicData getData() {
        return data;
    }

    public void setData(ResponseTopicData data) {
        this.data = data;
    }
}
