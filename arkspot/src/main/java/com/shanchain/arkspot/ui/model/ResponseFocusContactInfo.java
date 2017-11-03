package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/11/2.
 */

public class ResponseFocusContactInfo {


    /**
     * code : 000000
     * data : {"array":[{"letter":"Y","list":[{"characterId":10,"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg","intro":"少年杨戬有一个秘密，谁也不知道的秘密。额头内埋藏着\u201c眼\u201d。从外表看不出来，但无时不能深刻感知到其中蕴含的力量。没有人告诉他这是什么，但肯定有别于正常的人类。","modelNo":1,"name":"少年杨戬有一个秘密，谁也不知道的秘密。额头内埋藏着\u201c眼\u201d。从外表看不出来，但无时不能深刻感知到其中蕴含的力量。没有人告诉他这是什么，但肯定有别于正常的人类。","userId":6}]}]}
     * message : ok
     */

    private String code;
    private ResponseFocusDataBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResponseFocusDataBean getData() {
        return data;
    }

    public void setData(ResponseFocusDataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
