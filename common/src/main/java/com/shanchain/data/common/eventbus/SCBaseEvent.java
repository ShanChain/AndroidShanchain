package com.shanchain.data.common.eventbus;

/**
 * Created by flyye on 2017/11/7.
 */

public class SCBaseEvent {


    public String receiver;

    public String key;

    public Object params;


    public EventCallback callback;

   public SCBaseEvent(String receiver,String key,Object params,EventCallback callback){
        this.receiver = receiver;
        this.key = key;
        this.params = params;
        this.callback = callback;
    }
}
