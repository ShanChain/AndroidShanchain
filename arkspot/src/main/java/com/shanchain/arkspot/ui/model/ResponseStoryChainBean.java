package com.shanchain.arkspot.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class ResponseStoryChainBean {
    /**
     * detailIds : ["n1"]
     * count : 4
     */

    private int count;
    private List<String> detailIds;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<String> detailIds) {
        this.detailIds = detailIds;
    }
}
