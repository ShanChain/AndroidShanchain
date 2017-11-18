package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/27.
 */

public class ResponseStoryList {


    /**
     * code : 000000
     * data : [{"characterId":7,"commentCount":0,"content":"can i add pic story?","createTime":1508143601000,"genNum":1,"intro":"add some pic","lineNum":1,"locNum":1,"parentForkId":0,"rootId":1,"spaceId":16,"status":1,"storyId":1,"supportCount":0,"title":"picstory","transpond":3,"type":1,"updateTime":1508143601000},{"characterId":7,"commentCount":0,"content":"第二个故事","createTime":1508206167000,"genNum":1,"intro":"第二个故事","lineNum":1,"locNum":1,"parentForkId":0,"rootId":2,"spaceId":16,"status":1,"storyId":2,"supportCount":0,"title":"第二个故事","transpond":0,"type":2,"updateTime":1508206167000},{"characterId":68,"commentCount":0,"content":"第三个故事","createTime":1508206168000,"genNum":1,"intro":"第三个故事","lineNum":1,"locNum":1,"parentForkId":0,"rootId":3,"spaceId":16,"status":1,"storyId":3,"supportCount":2,"title":"第三个故事","transpond":0,"type":2,"updateTime":1508206168000},{"characterId":68,"commentCount":0,"content":"第四个故事","createTime":1508206169000,"genNum":1,"intro":"第四个故事","lineNum":1,"locNum":1,"parentForkId":0,"rootId":4,"spaceId":16,"status":1,"storyId":4,"supportCount":1,"title":"第四个故事","transpond":0,"type":2,"updateTime":1508206169000},{"characterId":11,"commentCount":2,"content":"第一个故事的第一个续集第二个故事","createTime":1508244780000,"genNum":2,"intro":" ","lineNum":1,"locNum":2,"parentForkId":0,"rootId":1,"spaceId":16,"status":1,"storyId":5,"supportCount":1,"title":" ","transpond":1,"type":1,"updateTime":1508244780000}]
     * message : ok
     */

    private String code;
    private String message;
    private List<CommentStoryInfo> data;

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

    public List<CommentStoryInfo> getData() {
        return data;
    }

    public void setData(List<CommentStoryInfo> data) {
        this.data = data;
    }
}
