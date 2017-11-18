package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/21.
 */

public class ResponseContactArr {

    /**
     * letter : C
     * list : [{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":13,"name":"程咬金"}]
     */

    private String letter;
    private List<ResponseContactBean> list;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<ResponseContactBean> getList() {
        return list;
    }

    public void setList(List<ResponseContactBean> list) {
        this.list = list;
    }
}
