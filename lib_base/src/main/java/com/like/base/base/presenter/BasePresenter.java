package com.like.base.base.presenter;

import android.content.Context;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by longshao on 2017/10/12.
 */

public abstract class BasePresenter<V> {

    protected WeakReference<V> mViewRef;
    protected Context context;
    protected CompositeDisposable disposables;

    public void attachContext(Context context) {
        this.context = context;
    }

    public void attachView(V view) {
        mViewRef = new WeakReference<>(view);
    }

    protected V getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if (disposables != null) {
            disposables.dispose();
            disposables = null;
        }
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public void addDisposable(Disposable disposable) {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        if (!disposables.isDisposed()) {
            disposables.add(disposable);
        }
    }

    public void removeDisposable(Disposable disposable) {
        if (disposables == null) return;
        if (!disposables.isDisposed()) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
            disposables.remove(disposable);
        }
    }

    public int getDisposableNumber() {
        if (disposables == null)
            return 0;
        return disposables.size();
    }
}
