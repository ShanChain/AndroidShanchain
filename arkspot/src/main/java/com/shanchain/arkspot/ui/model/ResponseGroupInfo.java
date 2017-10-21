package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/19.
 */

public class ResponseGroupInfo {


    /**
     * code : 000000
     * data : [{"allowinvites":true,"createTime":1503415128000,"groupDesc":"3231","groupId":"25057176911873","groupName":"1111","groupOwner":{"characterId":77,"hxPassword":"hx1503276332216","hxUserName":"sc2054380108"},"iconUrl":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/828fcb3fa0674e5cbd3dc1393af90191.jpg?x-oss-process=image/resize,m_fixed,h_1100,w_733","maxUsers":123,"membersOnly":false,"public":true,"valid":false},{"allowinvites":true,"createTime":1503415128000,"groupDesc":"3231","groupId":"25057176911873","groupName":"1111","groupOwner":{"characterId":77,"hxPassword":"hx1503276332216","hxUserName":"sc2054380108"},"iconUrl":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/828fcb3fa0674e5cbd3dc1393af90191.jpg?x-oss-process=image/resize,m_fixed,h_1100,w_733","maxUsers":123,"membersOnly":false,"public":true,"valid":false},{"allowinvites":false,"createTime":1503342979000,"groupDesc":"test group for dev","groupId":"25058636529665","groupName":"25058636529665","groupOwner":{"characterId":77,"hxPassword":"hx1503276332216","hxUserName":"sc2054380108"},"iconUrl":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/828fcb3fa0674e5cbd3dc1393af90191.jpg?x-oss-process=image/resize,m_fixed,h_1100,w_733","maxUsers":100,"membersOnly":false,"public":true,"valid":true}]
     * message : ok
     */

    private String code;
    private String message;
    private List<GroupInfo> data;

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

    public List<GroupInfo> getData() {
        return data;
    }

    public void setData(List<GroupInfo> data) {
        this.data = data;
    }
}
