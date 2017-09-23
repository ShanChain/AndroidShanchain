package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/9/22.
 */

public class FansListInfo {


    /**
     * data : [{"key":{"funsId":12,"characterId":23},"createTime":1503609332000},{"key":{"funsId":12,"characterId":23},"createTime":1503609344000}]
     * message : ok
     * code : 000000
     */

    private String message;
    private String code;
    private List<FansInfo> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FansInfo> getFansInfos() {
        return data;
    }

    public void setFansInfos(List<FansInfo> fansInfos) {
        this.data = fansInfos;
    }
}
