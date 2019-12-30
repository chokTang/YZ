package com.like.base.net.model;

public class Optional<T> {
    private T data;

    public Optional(T data) {
        this.data = data;
    }

    public boolean isEmpty(){
        return data==null;
    }

    public T get(){
        return data;
    }
}
