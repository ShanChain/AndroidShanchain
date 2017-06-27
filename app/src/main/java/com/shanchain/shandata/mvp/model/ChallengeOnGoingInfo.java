package com.shanchain.shandata.mvp.model;

/**
 * Created by zhoujian on 2017/6/20.
 */

public class ChallengeOnGoingInfo {

    /** 描述：
     * type = 1  :  多走走
     * type = 2  :  开心点
     * type = 3  :  早点睡
     * type = 4  :  别低头
     * type = 5  :  其他
     * */
    private int type;
    private String des;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
