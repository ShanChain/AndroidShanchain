package com.shanchain.arkspot.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/8/30.
 */

public class TopicInfo implements Serializable{
    private String topic;
    private String tag;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
