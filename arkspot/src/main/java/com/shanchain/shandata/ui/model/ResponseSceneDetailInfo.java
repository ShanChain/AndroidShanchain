package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/11/3.
 */

public class ResponseSceneDetailInfo {


    /**
     * code : 000000
     * message : ok
     * data : {"groupId":"31749455282179","groupName":"亚雄的对话场景","spaceId":140,"iconUrl":"","groupDesc":"亚雄创建的对话场景","maxUsers":100,"membersOnly":false,"allowinvites":false,"groupOwner":{"hxUserName":"sc-738726912","characterId":190,"userId":4,"hxPassword":"hx1509676446640"},"valid":true,"createTime":1509677041000,"public":true}
     */

    private String code;
    private String message;
    private SceneDetailData data;

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

    public SceneDetailData getData() {
        return data;
    }

    public void setData(SceneDetailData data) {
        this.data = data;
    }
}
