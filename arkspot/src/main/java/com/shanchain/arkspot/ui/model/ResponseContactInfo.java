package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/28.
 */

public class ResponseContactInfo {


    /**
     * code : 000000
     * message : ok
     * data : [{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg","intro":"\u201c蝴蝶是我，抑或我就是蝴蝶?\u201d\r\n\u201c是我在梦中邂逅了这个世界，抑或世界原本就是我的梦?\u201d","name":"周庄","modelNo":1,"type":1,"characterId":68,"userId":2},{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg","intro":"长久以来，大陆一直流传着关于灭世魔神王的传说。他象征着绝对的黑暗，要将人间界带入永夜和毁灭之中，而他也许的大魔王的转身。","name":"项羽","modelNo":1,"type":1,"characterId":69,"userId":25}]
     */

    private String code;
    private String message;
    private List<ContactBean> data;

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

    public List<ContactBean> getData() {
        return data;
    }

    public void setData(List<ContactBean> data) {
        this.data = data;
    }
}
