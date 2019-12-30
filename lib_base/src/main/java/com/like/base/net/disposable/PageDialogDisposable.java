package com.like.base.net.disposable;

import com.like.base.net.inter.IPageListener;

import io.reactivex.observers.DisposableObserver;

/**
 * 页面上统一控制对话框显示与否
 *
 * @param <T>
 */
public class PageDialogDisposable<T> extends DisposableObserver<T> {

    private IPageListener<T> listener;

    public PageDialogDisposable(IPageListener<T> listener) {
        this.listener = listener;
    }

    @Override
    protected void onStart() {
        if (listener != null) {
            listener.onStart(this);
        }
    }

    @Override
    public void onNext(T value) {
        if (listener != null) {
            listener.onNext(value, this);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (listener != null) {
            listener.onError(e, this);
        }
    }

    @Override
    public void onComplete() {
        dispose();
    }
}
