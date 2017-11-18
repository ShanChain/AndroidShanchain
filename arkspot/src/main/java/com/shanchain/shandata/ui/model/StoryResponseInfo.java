package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class StoryResponseInfo {


    /**
     * code : 000000
     * message : ok
     * data : [{"chain":{"detailIds":["n1"],"count":4},"detailId":"n5"},{"chain":"","detailId":"n4"},{"chain":"","detailId":"n3"},{"chain":"","detailId":"n2"},{"chain":{"detailIds":["s31"],"count":2},"detailId":"s35"},{"chain":{"detailIds":["s31"],"count":2},"detailId":"s36"},{"chain":{"detailIds":["s31","s33"],"count":3},"detailId":"s37"},{"chain":"","detailId":"s27"},{"chain":"","detailId":"s28"},{"chain":"","detailId":"s29"},{"chain":{"detailIds":[],"count":3},"detailId":"s31"},{"chain":"","detailId":"s32"},{"chain":{"detailIds":["s31"],"count":3},"detailId":"s33"},{"chain":{"detailIds":["s1"],"count":4},"detailId":"s6"},{"chain":{"detailIds":["s1"],"count":2},"detailId":"s7"},{"chain":{"detailIds":["s1","s5"],"count":4},"detailId":"s8"},{"chain":{"detailIds":["s1","s5","s8"],"count":4},"detailId":"s9"},{"chain":{"detailIds":["s1","s6"],"count":4},"detailId":"s10"},{"chain":{"detailIds":["s1","s6","s10"],"count":4},"detailId":"s11"},{"chain":{"detailIds":["s1","s6"],"count":3},"detailId":"s12"},{"chain":{"detailIds":["s1","s6"],"count":3},"detailId":"s13"},{"chain":{"detailIds":[],"count":4},"detailId":"s1"},{"chain":"[]","detailId":"t6"},{"chain":"[]","detailId":"t2"},{"chain":"[]","detailId":"t8"}]
     */

    private String code;
    private String message;
    private ResponseStoryIdData data;

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

    public ResponseStoryIdData getData() {
        return data;
    }

    public void setData(ResponseStoryIdData data) {
        this.data = data;
    }
}
