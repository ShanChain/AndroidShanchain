package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class ResponseStoryListInfo {

    /**
     * code : 000000
     * message : ok
     * data : [{"characterImg":"","img":"http://www.baidu.com","supportCount":2,"rootId":0,"detailId":"t2","title":"送给三次元的慰问","type":3,"isFav":0,"spaceId":16,"tailId":0,"commendCount":1,"genNum":0,"tailName":"","createTime":1501636665000,"intro":"今天让我们隔空向三次元送去慰问，让大叔大妈们更加了解我们。让我们荡起双桨，把三次元征服。今天让我们隔空向三次元送去慰问\u2026\u2026","lineNum":0,"characterId":2,"status":1,"transpond":0},{"characterImg":"","img":"333333","supportCount":1,"rootId":0,"detailId":"t6","title":"一起结婚吧","type":3,"isFav":0,"spaceId":16,"tailId":0,"commendCount":3,"genNum":0,"tailName":"","createTime":1504012204000,"intro":"啦啦啦啦安拉啊","lineNum":0,"characterId":2,"status":1,"transpond":0},{"characterImg":"","img":"","supportCount":0,"rootId":1,"detailId":"s1","title":"picstory","type":1,"isFav":0,"spaceId":16,"tailId":12,"commendCount":0,"genNum":1,"tailName":"","createTime":1508143601000,"intro":"add some pic","lineNum":1,"characterId":7,"status":1,"transpond":3},{"characterImg":"","img":"","supportCount":2,"rootId":3,"detailId":"n3","title":"第三个故事","type":2,"isFav":0,"spaceId":16,"tailId":12,"commendCount":0,"genNum":1,"tailName":"","createTime":1508206168000,"intro":"第三个故事","lineNum":1,"characterId":68,"status":1,"transpond":0},{"characterImg":"","img":"","supportCount":1,"rootId":4,"detailId":"n4","title":"第四个故事","type":2,"isFav":0,"spaceId":16,"tailId":12,"commendCount":0,"genNum":1,"tailName":"","createTime":1508206169000,"intro":"第四个故事","lineNum":1,"characterId":68,"status":1,"transpond":0},{"characterImg":"","img":"","supportCount":1,"rootId":1,"detailId":"n5","title":" ","type":2,"isFav":0,"spaceId":16,"tailId":12,"commendCount":2,"genNum":2,"tailName":"","createTime":1508206174000,"intro":" ","lineNum":1,"characterId":11,"status":1,"transpond":1}]
     */

    private String code;
    private String message;
    private List<StoryModelBean> data;

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

    public List<StoryModelBean> getData() {
        return data;
    }

    public void setData(List<StoryModelBean> data) {
        this.data = data;
    }
}
