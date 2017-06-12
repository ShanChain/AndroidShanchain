package com.shanchain.mvp.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/6/7.
 */

public class TopicInfo implements Serializable{
    private String topic;
    private boolean isNew;

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
