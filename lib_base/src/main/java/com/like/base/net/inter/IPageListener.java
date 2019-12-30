package com.like.base.net.inter;

import io.reactivex.disposables.Disposable;

public interface IPageListener<T> {
    void onStart(Disposable disposable);

    void onNext(T value, Disposable disposable);

    void onError(Throwable e, Disposable disposable);
}
