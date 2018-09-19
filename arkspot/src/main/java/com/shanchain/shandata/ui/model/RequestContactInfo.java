package com.shanchain.shandata.ui.model;

/**
 * Created by zhoujian on 2017/10/21.
 */

public class RequestContactInfo {


    /**
     * code : 000000
     * message : ok
     * data : {"array":[{"letter":"C","list":[{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":13,"name":"程咬金"}]},{"letter":"T","list":[{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":9,"name":"太乙真人"}]},{"letter":"X","list":[{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":19,"name":"夏侯惇"},{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":10,"name":"项羽"}]},{"letter":"Y","list":[{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":14,"name":"雅典娜"},{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":17,"name":"杨戬"}]},{"letter":"Z","list":[{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":15,"name":"周庄"}]},{"letter":"L","list":[{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":16,"name":"刘邦"},{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":11,"name":"刘禅"}]},{"letter":"M","list":[{"headImg":"http://shanchain-seller.oss-cn-hongkong.aliyuncs.com/81a0a74dc8034ab488e53eaf0e176bd4.jpg","modelId":12,"name":"墨子"}]}]}
     */

    private String code;
    private String message;
    private ResponseContactData data;

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

    public ResponseContactData getData() {
        return data;
    }

    public void setData(ResponseContactData data) {
        this.data = data;
    }
}
