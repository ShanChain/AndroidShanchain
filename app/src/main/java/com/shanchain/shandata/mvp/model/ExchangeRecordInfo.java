package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/7/11.
 */

public class ExchangeRecordInfo {
    private String goodsName;
    private String orderNum;
    private String time;
    private int coins;


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
