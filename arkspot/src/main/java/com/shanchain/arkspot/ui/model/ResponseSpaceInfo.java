package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/18.
 */

public class ResponseSpaceInfo {


    /**
     * code : 000000
     * message : ok
     * data : [{"spaceId":16,"name":"王者荣耀","slogan":"欢迎来到王者荣耀，敌军还有5秒到达战场！","intro":"无限时空中，时光的洪流汇聚于同一片大陆，机关术与魔道肆虐，让世界大地面目全非。\r\n英雄，那些熟知的名字，不可思议地汇集在一起，而你的到来，将会改变什么？","disc":"423234423442342112","bgPic":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/f927168343c540ef87f60882530e8dde.jpg","status":1,"createBy":1,"createTime":1505876096000,"updateTime":1507536822000,"characterModelNum":11,"favoriteNum":2,"tagMap":[{"tagId":52,"tagName":"手游","rate":0}]}]
     */

    private String code;
    private String message;
    private List<SpaceDetailInfo> data;

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

    public List<SpaceDetailInfo> getData() {
        return data;
    }

    public void setData(List<SpaceDetailInfo> data) {
        this.data = data;
    }
}
