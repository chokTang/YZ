package com.like.base.base.rxjava;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    private final PublishSubject<Object> subject=PublishSubject.create();

    private static final class Holder {
        private static final RxBus intance = new RxBus();
    }

    public static final RxBus getInstance() {
        return Holder.intance;
    }

    public void post(Object value){
        subject.onNext(value);
    }

    public <T> Observable<T> toObservable(Class<T> eventType){
        return subject.ofType(eventType);
    }
}
