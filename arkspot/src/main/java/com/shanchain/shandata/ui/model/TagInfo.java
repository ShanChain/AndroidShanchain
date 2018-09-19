package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/10/10.
 */

public class TagInfo implements Serializable{

    /**
     * code : 000000
     * message : ok
     * data : {"content":[{"tagId":24,"tagName":"前轴心时代","rate":0},{"tagId":25,"tagName":"原创","rate":0},{"tagId":26,"tagName":"历史","rate":0},{"tagId":27,"tagName":"动漫","rate":0},{"tagId":28,"tagName":"游戏","rate":0},{"tagId":29,"tagName":"小说","rate":0},{"tagId":30,"tagName":"影视","rate":0},{"tagId":31,"tagName":"娱乐圈","rate":0},{"tagId":32,"tagName":"古风","rate":0},{"tagId":33,"tagName":"现代","rate":0},{"tagId":34,"tagName":"耽美","rate":0},{"tagId":35,"tagName":"百合","rate":0},{"tagId":36,"tagName":"商界","rate":0},{"tagId":37,"tagName":"体育","rate":0},{"tagId":38,"tagName":"校园","rate":0},{"tagId":39,"tagName":"玄幻","rate":0},{"tagId":40,"tagName":"奇幻","rate":0},{"tagId":41,"tagName":"武侠","rate":0},{"tagId":42,"tagName":"仙侠","rate":0},{"tagId":43,"tagName":"科幻","rate":0}],"last":false,"totalPages":3,"totalElements":47,"sort":null,"numberOfElements":20,"first":true,"size":20,"number":0}
     */

    private String code;
    private String message;
    private TagDataBean data;

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

    public TagDataBean getData() {
        return data;
    }

    public void setData(TagDataBean data) {
        this.data = data;
    }
}
