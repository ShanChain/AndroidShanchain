package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/23.
 */

public class ResponseCommentInfo {


    /**
     * code : 000000
     * message : ok
     * data : [{"commentId":13,"storyId":8,"characterId":68,"content":"蝴蝶如我，我如蝴蝶","supportCount":0,"createTime":1507639682000,"isAnon":0,"userId":null,"mySupport":false}]
     */

    private String code;
    private String message;
    private CommentData data;

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

    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }
}
