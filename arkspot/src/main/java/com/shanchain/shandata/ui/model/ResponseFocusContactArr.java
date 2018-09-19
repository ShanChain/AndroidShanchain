package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/11/2.
 */

public class ResponseFocusContactArr {

    /**
     * letter : Y
     * list : [{"characterId":10,"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg","intro":"少年杨戬有一个秘密，谁也不知道的秘密。额头内埋藏着\u201c眼\u201d。从外表看不出来，但无时不能深刻感知到其中蕴含的力量。没有人告诉他这是什么，但肯定有别于正常的人类。","modelNo":1,"name":"少年杨戬有一个秘密，谁也不知道的秘密。额头内埋藏着\u201c眼\u201d。从外表看不出来，但无时不能深刻感知到其中蕴含的力量。没有人告诉他这是什么，但肯定有别于正常的人类。","userId":6}]
     */

    private String letter;
    private List<ResponseFocusContactBean> list;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public List<ResponseFocusContactBean> getList() {
        return list;
    }

    public void setList(List<ResponseFocusContactBean> list) {
        this.list = list;
    }
}
