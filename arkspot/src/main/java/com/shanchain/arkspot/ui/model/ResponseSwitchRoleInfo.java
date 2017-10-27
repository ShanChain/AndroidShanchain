package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/26.
 */

public class ResponseSwitchRoleInfo {


    /**
     * code : 000000
     * message : ok
     * data : {"characterId":103,"userId":4,"name":"墨子","intro":"上古时代创造了极为辉煌的文明，其中最伟大的发明之一便是机关术。但随着上古时代的终结，机关术的奥秘也逐渐湮没。谁也没想到的是，机关术重新复兴，是通过一名普通的平民工匠之手。","disc":"  ","headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","spaceId":16,"modelId":12,"modelNo":3,"signature":"","status":1,"createTime":1509006070069}
     */

    private String code;
    private String message;
    private CharacterInfo data;

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

    public CharacterInfo getData() {
        return data;
    }

    public void setData(CharacterInfo data) {
        this.data = data;
    }
}
