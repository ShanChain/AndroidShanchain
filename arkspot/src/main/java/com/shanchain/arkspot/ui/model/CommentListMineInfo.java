package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/27.
 */

public class CommentListMineInfo {

    /**
     * code : 000000
     * message : ok
     * data : {"content":[{"commentId":55,"storyId":108,"characterId":116,"content":"减减肥","supportCount":0,"createTime":1509074783000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":54,"storyId":108,"characterId":116,"content":"比较更多的","supportCount":0,"createTime":1509074655000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":53,"storyId":108,"characterId":116,"content":"看见刚刚","supportCount":0,"createTime":1509074619000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":52,"storyId":109,"characterId":120,"content":"叽叽咕咕","supportCount":0,"createTime":1509067979000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":51,"storyId":98,"characterId":119,"content":"羡慕羡慕东西联系你","supportCount":0,"createTime":1509021300000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":50,"storyId":98,"characterId":119,"content":"欧豪","supportCount":0,"createTime":1509021293000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":49,"storyId":98,"characterId":119,"content":"多辛苦快下课那些","supportCount":0,"createTime":1509021288000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":48,"storyId":98,"characterId":119,"content":"想你下节课","supportCount":0,"createTime":1509021284000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":47,"storyId":95,"characterId":117,"content":"就到家","supportCount":0,"createTime":1509019391000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":46,"storyId":93,"characterId":116,"content":"家家户户","supportCount":0,"createTime":1509019098000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":45,"storyId":94,"characterId":109,"content":"就到家大家好","supportCount":0,"createTime":1509016114000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":44,"storyId":94,"characterId":109,"content":"奖学金鸡蛋糕","supportCount":0,"createTime":1509016111000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":43,"storyId":91,"characterId":9,"content":"才能吃姐姐","supportCount":0,"createTime":1509010347000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":42,"storyId":91,"characterId":9,"content":"几个","supportCount":0,"createTime":1509005231000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":41,"storyId":91,"characterId":9,"content":"不回复","supportCount":0,"createTime":1509005201000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":40,"storyId":2,"characterId":9,"content":"机会","supportCount":0,"createTime":1508917498000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":39,"storyId":91,"characterId":9,"content":"几个发动机","supportCount":0,"createTime":1508809673000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":37,"storyId":87,"characterId":9,"content":"姐姐发个","supportCount":0,"createTime":1508762300000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":36,"storyId":89,"characterId":9,"content":"回家换个","supportCount":0,"createTime":1508762192000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":35,"storyId":90,"characterId":9,"content":"看见那你","supportCount":0,"createTime":1508762177000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":34,"storyId":89,"characterId":9,"content":"那就好好","supportCount":0,"createTime":1508761843000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":33,"storyId":90,"characterId":9,"content":"哈哈哈哈哈哈哈哈！","supportCount":0,"createTime":1508761776000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":32,"storyId":90,"characterId":9,"content":"好几个","supportCount":0,"createTime":1508761751000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":31,"storyId":89,"characterId":9,"content":"哈哈哈，笑死我了","supportCount":0,"createTime":1508761291000,"isAnon":0,"userId":4,"mySupport":false},{"commentId":38,"storyId":91,"characterId":9,"content":"就好好","supportCount":0,"createTime":1507772868000,"isAnon":0,"userId":4,"mySupport":false}],"last":true,"totalPages":1,"totalElements":25,"sort":[{"direction":"DESC","property":"createTime","ignoreCase":false,"nullHandling":"NATIVE","ascending":false,"descending":true}],"first":true,"numberOfElements":25,"size":100,"number":0}
     */

    private String code;
    private String message;
    private CommentMineData data;

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

    public CommentMineData getData() {
        return data;
    }

    public void setData(CommentMineData data) {
        this.data = data;
    }
}
