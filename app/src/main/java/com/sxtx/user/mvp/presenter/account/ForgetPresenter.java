package com.sxtx.user.mvp.presenter.account;

import com.like.base.base.BaseFragment;
import com.like.base.net.disposable.SingleDialogDisposable;
import com.like.base.net.inter.SubscriberOnNextListener;
import com.like.base.net.map.HttpResultStringFunc;
import com.sxtx.user.mvp.APPresenter;
import com.sxtx.user.mvp.view.account.IForgetView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/30.
 */

public class ForgetPresenter extends APPresenter<IForgetView> {

    /**
     * 发送验证码
     */
    public void toSendCode(String phone) {
        SingleDialogDisposable<String> singleDialogDisposable = new SingleDialogDisposable<String>(context)
                .bindCallbace(new SubscriberOnNextListener<String>() {
                    @Override
                    public void onNext(String s) {
                        if (isViewAttached()) {
                            getView().onShowSendCodeResult(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onShowSendCodeResult(false);
                        }
                    }
                });
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code_type", 1);
        params.put("user_role", 0);
//        noTokenApi.sendCode(paramsMd5(params))
//                .map(new HttpResultStringFunc<String>())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(singleDialogDisposable);
    }

    /**
     * 修改密码
     */
    public void onModifyPassword(String phone, String code, String oldPassword, String newPassword) {
        SingleDialogDisposable<String> singleDialogDisposable = new SingleDialogDisposable<String>(context)
                .bindCallbace(new SubscriberOnNextListener<String>() {
                    @Override
                    public void onNext(String s) {
                        if (isViewAttached()) {
                            getView().onModityPasswordResult(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            ((BaseFragment) getView()).showToast(e.getMessage());
                        }
                    }
                });
        Map<String, Object> params = new HashMap<>();
        params.put("user_phone", phone);
        params.put("old_pass", oldPassword);
        params.put("new_pass", newPassword);
        params.put("verify_code", code);
        accountApi.onModifyPassword(paramsDeal(params))
                .map(new HttpResultStringFunc<String>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(singleDialogDisposable);
    }
}
