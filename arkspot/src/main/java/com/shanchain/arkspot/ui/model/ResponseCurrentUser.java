package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/27.
 */

public class ResponseCurrentUser {


    /**
     * code : 000000
     * data : {"characterId":69,"createTime":1506050953000,"disc":"","headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg","intro":"长久以来，大陆一直流传着关于灭世魔神王的传说。他象征着绝对的黑暗，要将人间界带入永夜和毁灭之中，而他也许的大魔王的转身。","modelId":10,"modelNo":1,"name":"项羽","signature":"蝴蝶是我，我是蝴蝶","spaceId":16,"status":1,"userId":25}
     * message : ok
     */

    private String code;
    private CharacterInfo data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CharacterInfo getData() {
        return data;
    }

    public void setData(CharacterInfo data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
