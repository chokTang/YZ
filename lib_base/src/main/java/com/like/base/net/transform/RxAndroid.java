package com.like.base.net.transform;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * rxjava2 的调度
 *
 * @author shidengzhong
 * 2019年7月16日 10:54:52
 */
public class RxAndroid {

    private static final class Holder {
        private static final RxAndroid intance = new RxAndroid();
    }

    public static RxAndroid newIntance() {
        return Holder.intance;
    }

    /**
     * 线程的调度
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
