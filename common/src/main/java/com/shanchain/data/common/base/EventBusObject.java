package com.shanchain.data.common.base;

/**
 * Created by WealChen
 * Date : 2019/2/19
 * Describe :
 */
public class EventBusObject<T> {

    private int code;
    private T data;

    public EventBusObject(T data) {
        this.data = data;
    }

    public EventBusObject(int code) {
        this.code = code;
    }

    public EventBusObject(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }

}
