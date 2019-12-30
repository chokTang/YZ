package com.like.base.net.inter;

/**
 * Created by Administrator on 2016/6/20 0020.
 */

public interface SubscriberOnNextListener<T> {
    void onNext(T t);
    void onError(Throwable e);
}
