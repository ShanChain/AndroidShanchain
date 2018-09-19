package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/19.
 */

public class ReleaseContentInfo {

    private String content;
    private List<String> imgs;
    private List<SpanBean> spanBeanList;

    public List<SpanBean> getSpanBeanList() {
        return spanBeanList;
    }

    public void setSpanBeanList(List<SpanBean> spanBeanList) {
        this.spanBeanList = spanBeanList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
