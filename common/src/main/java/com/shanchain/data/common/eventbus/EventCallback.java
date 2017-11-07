package com.shanchain.data.common.eventbus;

/**
 * Created by flyye on 2017/11/7.
 */

public interface EventCallback {
    void invoke(String receiver,Object result);
}
