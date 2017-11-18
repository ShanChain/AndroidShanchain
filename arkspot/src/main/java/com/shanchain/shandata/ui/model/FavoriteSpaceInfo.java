package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/16.
 */

public class FavoriteSpaceInfo {


    /**
     * code : 000000
     * message : ok
     * data : [{"spaceId":2,"name":"夏商周","slogan":"文明的少年时代","intro":"夏商周三代英雄辈出，文明的基因逐渐形成","disc":"我的","bgPic":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/cf174fdcc3d147d29ac253f3634a8230.jpg","status":0,"createBy":1,"createTime":1505876061000,"updateTime":1507620140000,"characterModelNum":0,"favoriteNum":3,"tagMap":[]},{"spaceId":4,"name":"三体","slogan":"给岁月以文明，给时光以生命。今后我们是同志了。","intro":"《三体》是刘慈欣创作的系列长篇科幻小说，由《三体》《三体Ⅱ·黑暗森林》《三体Ⅲ·死神永生》组成。作品讲述了地球人类文明和三体文明的信息交流、生死搏杀及两个文明在宇宙中的兴衰历程。其第一部经过刘宇昆翻译后获得了第73届雨果奖最佳长篇小说奖。","disc":" ","bgPic":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/5a7cb7a2edd74d3398cf772bb21f2486.jpg","status":0,"createBy":1,"createTime":1505875720000,"updateTime":1507959632000,"characterModelNum":4,"favoriteNum":4,"tagMap":[{"tagId":29,"tagName":"小说","rate":0},{"tagId":26,"tagName":"历史","rate":0},{"tagId":28,"tagName":"游戏","rate":0}]},{"spaceId":16,"name":"王者荣耀","slogan":"欢迎来到王者荣耀，敌军还有5秒到达战场！","intro":"无限时空中，时光的洪流汇聚于同一片大陆，机关术与魔道肆虐，让世界大地面目全非。\r\n英雄，那些熟知的名字，不可思议地汇集在一起，而你的到来，将会改变什么？","disc":"42323442344234","bgPic":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/f927168343c540ef87f60882530e8dde.jpg","status":1,"createBy":1,"createTime":1505876096000,"updateTime":1507536822000,"characterModelNum":11,"favoriteNum":2,"tagMap":[{"tagId":52,"tagName":"手游","rate":0}]},{"spaceId":22,"name":"假面派对","slogan":"面具是假的，我是真的。表达更真实的自己~","intro":" ","disc":" ","bgPic":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/35b5528047d64d46980456f8d09f91a0.jpg","status":1,"createBy":1,"createTime":1505876127000,"updateTime":1507959682000,"characterModelNum":0,"favoriteNum":2,"tagMap":[]},{"spaceId":32,"name":"韩娱专区","slogan":"这里是韩星们的家！","intro":"今天开始我可以爱你么，第一次清楚地感觉不想放弃，爱情似乎要降临，我永远要给你最好地，我可以爱你么？","disc":" ","bgPic":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/c3a695931ee14c7eb2186feace30d8b6.jpg","status":1,"createBy":1,"createTime":1505897091000,"updateTime":1507959637000,"characterModelNum":12,"favoriteNum":2,"tagMap":[{"tagId":31,"tagName":"娱乐圈","rate":0}]}]
     */

    private String code;
    private String message;
    private List<FavoriteSpaceBean> data;

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

    public List<FavoriteSpaceBean> getData() {
        return data;
    }

    public void setData(List<FavoriteSpaceBean> data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "FavoriteSpaceInfo{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
