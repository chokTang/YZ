package com.like.base.base.rxjava;

public class CommonEvent {
    private int code;
    private Object data;

    public CommonEvent(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public CommonEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
