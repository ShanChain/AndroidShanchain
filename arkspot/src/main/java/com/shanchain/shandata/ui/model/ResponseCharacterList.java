package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/23.
 */

public class ResponseCharacterList {


    /**
     * code : 000000
     * message : ok
     * data : [{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg","intro":"出自手游《王者荣耀》，作为一名炼金师，太乙真人大部分时间都宅在家里研究各种稀奇古怪的东西，现在是时候把这些发明拿到战场上试验一下威力了~","name":"太乙真人","modelNo":1,"characterId":9,"userId":4},{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/193a2c667de440b595a790bc5e7c7d69.jpg","intro":"楚汉之地，东方最神秘的地域之一。虔诚的大河子民中，他那样另类：既无视信仰，又热衷利益，更不择手段。","name":"刘邦","modelNo":2,"characterId":11,"userId":25}]
     */

    private String code;
    private String message;
    private List<ResponseCharacterBrief> data;

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

    public List<ResponseCharacterBrief> getData() {
        return data;
    }

    public void setData(List<ResponseCharacterBrief> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseCharacterList{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
