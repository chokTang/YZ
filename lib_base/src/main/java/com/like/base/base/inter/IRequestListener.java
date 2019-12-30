package com.like.base.base.inter;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/4/23.
 */

public interface IRequestListener<T> {
    Observable onCreateObservable();

    void onSuccess(T t);

    void onFail(String message);
}
