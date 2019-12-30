package com.like.base.net.disposable;

import android.content.Context;

import com.like.base.net.inter.RequestCancelListener;
import com.like.base.net.inter.SubscriberOnNextListener;
import com.like.utilslib.other.LogUtil;

import io.reactivex.observers.DisposableObserver;

/**
 * 每请求一次网络都会创建一个dialog
 * Created by longshao on 2016/6/20 0020.
 * 注：T泛型是指的HttpResult中的泛型
 */

public class SingleDialogDisposable<T> extends DisposableObserver<T> implements RequestCancelListener {

    private SubscriberOnNextListener<T> subscriberOnNextListener;
    private Context context;
    private RequestDialogHandler handler;
    private boolean _isShowDialog = true;

    public SingleDialogDisposable(Context context) {
        this.context = context;
        handler = new RequestDialogHandler(context, this);
    }

    public SingleDialogDisposable(Context context, SubscriberOnNextListener<T> subscriberOnNextListener) {
        this.context = context;
        this.subscriberOnNextListener = subscriberOnNextListener;
        handler = new RequestDialogHandler(context, this);
    }

    public SingleDialogDisposable<T> bindCallbace(SubscriberOnNextListener<T> subscriberOnNextListener) {
        this.subscriberOnNextListener = subscriberOnNextListener;
        return this;
    }

    public SingleDialogDisposable isShowDilog(boolean iShowdialog) {
        _isShowDialog = iShowdialog;
        return this;
    }

    @Override
    protected void onStart() {
        LogUtil.loge("SingleDialogDisposable---onStart()");
        if (_isShowDialog) {
            showProgressDialog();
        }
    }

    @Override
    public void onNext(T t) {
        LogUtil.loge(t.toString());
        if (subscriberOnNextListener != null)
            subscriberOnNextListener.onNext(t);
    }

    @Override
    public void onComplete() {
        LogUtil.loge("SingleDialogDisposable---onComplete()");
        if (_isShowDialog) {
            dismissProgressDialog();
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.loge(e.toString());
        if (_isShowDialog) {
            dismissProgressDialog();
        }
        if (subscriberOnNextListener != null)
            subscriberOnNextListener.onError(e);
    }

    /**
     * 取消网络请求
     */
    @Override
    public void onCancleRequest() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    /**
     * 显示对话框
     */
    private void showProgressDialog() {
        if (handler == null) {
            handler = new RequestDialogHandler(context, this);
        }
        if (handler != null) {
            handler.obtainMessage(RequestDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    /**
     * 关闭对话框
     */
    private void dismissProgressDialog() {
        if (handler != null) {
            handler.obtainMessage(RequestDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            handler = null;
        }
    }
}
