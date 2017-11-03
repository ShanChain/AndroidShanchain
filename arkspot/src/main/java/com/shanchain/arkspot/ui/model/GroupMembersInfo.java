package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/3.
 */

public class GroupMembersInfo {


    /**
     * code : 000000
     * data : [{"admin":true,"blocked":false,"characterId":0,"groupId":"25058636529665","hxUserName":"sc2054380109","id":6},{"admin":false,"blocked":false,"characterId":0,"groupId":"25058636529665","hxUserName":"sc2054380111","id":7},{"admin":false,"blocked":false,"characterId":0,"groupId":"25058636529665","hxUserName":"ddhfieio","id":5},{"admin":false,"blocked":false,"characterId":0,"groupId":"25058636529665","hxUserName":"sc2054380142","id":8},{"admin":false,"blocked":false,"characterId":0,"groupId":"25058636529665","hxUserName":"20","id":4}]
     * message : ok
     */

    private String code;
    private String message;
    private List<ResponseGroupMemberBean> data;

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

    public List<ResponseGroupMemberBean> getData() {
        return data;
    }

    public void setData(List<ResponseGroupMemberBean> data) {
        this.data = data;
    }
}
