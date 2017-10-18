package com.shanchain.arkspot.ui.model;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class ResponseStoryIdBean {
    /**
     * chain : {"detailIds":["n1"],"count":4}
     * detailId : n5
     */

    private ResponseStoryChainBean chain;
    private String detailId;

    public ResponseStoryChainBean getChain() {
        return chain;
    }

    public void setChain(ResponseStoryChainBean chain) {
        this.chain = chain;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
