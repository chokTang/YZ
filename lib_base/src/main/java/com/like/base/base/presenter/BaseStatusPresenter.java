package com.like.base.base.presenter;

import com.like.base.base.BaseActivity;
import com.like.base.base.BaseFragment;
import com.like.base.base.inter.IRequestListener;
import com.like.base.net.disposable.PageDialogDisposable;
import com.like.base.net.disposable.SingleDialogDisposable;
import com.like.base.net.inter.IPageListener;
import com.like.base.net.inter.SubscriberOnNextListener;
import com.like.base.net.map.HttpResultFunc;
import com.like.base.net.transform.RxAndroid;
import com.like.utilslib.other.NetUtil;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/4/23.
 */

public class BaseStatusPresenter<V> extends BasePresenter<V> {

    protected BaseFragment fragment;
    private BaseActivity mActivity;

    @Override
    public void attachView(V view) {
        super.attachView(view);
        if (view instanceof BaseFragment) {
            fragment = (BaseFragment) view;
        } else if (view instanceof BaseActivity) {
            mActivity = (BaseActivity) view;
        }
    }

    protected <T> void onRequestData(boolean isShowDialog, final IRequestListener<T> listener) {
        if (!NetUtil.isConnected()) {
            if (fragment != null) {
                fragment.showView(BaseFragment.NONET_VIEW);
            }
            return;
        }

        SingleDialogDisposable<T> singleDialogDisposable = new SingleDialogDisposable<>(context);
        singleDialogDisposable.isShowDilog(isShowDialog);
        singleDialogDisposable.bindCallbace(new SubscriberOnNextListener<T>() {
            @Override
            public void onNext(T s) {
                if (isViewAttached()) {
                    if (fragment != null) {
                        fragment.showView(BaseFragment.MAIN_VIEW);
                    }
                    if (listener != null) {
                        listener.onSuccess(s);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    if (fragment != null) {
                        fragment.showView(BaseFragment.ERROR_VIEW, e.getMessage());
                    }
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }
            }
        });

        listener.onCreateObservable()
                .compose(RxAndroid.newIntance().io_main())
                .map(new HttpResultFunc<T>())
                .subscribe(singleDialogDisposable);
    }

    protected <T> void onRequestData(final IRequestListener<T> listener) {
        onRequestData(true, listener);
    }

    /**
     * 请求无拦截
     *
     * @param isShowDialog
     * @param listener
     * @param <T>
     */
    protected <T> void onRequestDataNoMap(boolean isShowDialog, final IRequestListener<T> listener) {
        if (!NetUtil.isConnected()) {
            if (fragment != null) {
                fragment.showView(BaseFragment.NONET_VIEW);
            }
            return;
        }

        SingleDialogDisposable<T> singleDialogDisposable = new SingleDialogDisposable<>(context);
        singleDialogDisposable.isShowDilog(isShowDialog);
        singleDialogDisposable.bindCallbace(new SubscriberOnNextListener<T>() {
            @Override
            public void onNext(T s) {
                if (isViewAttached()) {
                    if (fragment != null) {
                        fragment.showView(BaseFragment.MAIN_VIEW);
                    }
                    if (listener != null) {
                        listener.onSuccess(s);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    if (fragment != null) {
                        fragment.showView(BaseFragment.ERROR_VIEW, e.getMessage());
                    }
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }
            }
        });

        listener.onCreateObservable()
                .compose(RxAndroid.newIntance().io_main())
                .subscribe(singleDialogDisposable);
    }

    /**
     * 基于页面来请求数据
     *
     * @param isShowDialog
     * @param listener
     * @param <T>
     */
    protected <T> void onPageRequestData(final boolean isShowDialog, final IRequestListener<T> listener) {
        if (!NetUtil.isConnected()) {
            if (fragment != null) {
                fragment.showView(BaseFragment.NONET_VIEW);
            }
            return;
        }
        PageDialogDisposable<T> disposable = new PageDialogDisposable<>(new IPageListener<T>() {
            @Override
            public void onStart(Disposable disposable) {
                if (isViewAttached()) {
                    if (isShowDialog) {
                        showDialog();
                    }
                    addDisposable(disposable);
                }
            }

            @Override
            public void onNext(T value, Disposable disposable) {
                if (isViewAttached()){
                    removeDisposable(disposable);
                    hideDialog();
                    if (fragment != null) {
                        fragment.showView(BaseFragment.MAIN_VIEW);
                    }
                    if (listener != null) {
                        listener.onSuccess(value);
                    }
                }
            }

            @Override
            public void onError(Throwable e, Disposable disposable) {
                if (isViewAttached()){
                    removeDisposable(disposable);
                    hideDialog();
                    if (fragment != null) {
                        fragment.showView(BaseFragment.ERROR_VIEW, e.getMessage());
                    }
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }
            }
        });
        listener.onCreateObservable()
                .compose(RxAndroid.newIntance().io_main())
                .map(new HttpResultFunc<T>())
                .subscribe(disposable);
    }

    /**
     * 基于页面来请求数据
     *
     * @param isShowDialog
     * @param listener
     * @param <T>
     */
    protected <T> void onPageNoRequestData(final boolean isShowDialog, final IRequestListener<T> listener) {
        if (!NetUtil.isConnected()) {
            if (fragment != null) {
                fragment.showView(BaseFragment.NONET_VIEW);
            }
            return;
        }
        PageDialogDisposable<T> disposable = new PageDialogDisposable<>(new IPageListener<T>() {
            @Override
            public void onStart(Disposable disposable) {
                if (isViewAttached()){
                    if (isShowDialog) {
//                        showDialog();
                    }
                    addDisposable(disposable);
                }
            }

            @Override
            public void onNext(T value, Disposable disposable) {
                if (isViewAttached()) {
                    removeDisposable(disposable);
                    hideDialog();
                    if (fragment != null) {
                        fragment.showView(BaseFragment.MAIN_VIEW);
                    }
                    if (listener != null) {
                        listener.onSuccess(value);
                    }
                }
            }

            @Override
            public void onError(Throwable e, Disposable disposable) {
                if (isViewAttached()) {
                    removeDisposable(disposable);
                    hideDialog();
                    if (fragment != null) {
                        fragment.showView(BaseFragment.ERROR_VIEW, e.getMessage());
                    }
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }
            }
        });
        listener.onCreateObservable()
                .compose(RxAndroid.newIntance().io_main())
                .subscribe(disposable);
    }

    /**
     * 显示toast
     *
     * @param message
     */
    protected final void showFragmentToast(String message) {
        if (isViewAttached()) {
            if (fragment != null) {
                fragment.showToast(message);
            } else if (mActivity != null) {
                mActivity.showToast(message);
            }
        }
    }

    protected void showDialog() {
        if (fragment != null) {
            fragment.showDialog();
        }
        if (mActivity != null) {
            mActivity.showDialog();
        }
    }

    protected void hideDialog() {
        if (fragment != null) {
            fragment.hideDialog();
        }
        if (mActivity != null) {
            mActivity.hideDialog();
        }
    }
}
