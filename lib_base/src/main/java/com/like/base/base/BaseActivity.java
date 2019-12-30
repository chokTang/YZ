package com.like.base.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.like.base.R;
import com.like.base.base.inter.IBaseActivity;
import com.like.base.base.presenter.BasePresenter;
import com.like.base.base.rxjava.CommonEvent;
import com.like.base.base.rxjava.RxBus;
import com.like.base.net.widget.HttpResultDialog;
import com.like.utilslib.app.ActivityUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by longshao on 2017/7/27.
 */

public abstract class BaseActivity<P extends BasePresenter> extends SupportActivity implements IBaseActivity {

    private Toast mToast;
    private HttpResultDialog dialog;

    protected View mView;

    protected P mPresenter;

    public abstract BaseFragment setRootFragment();//设置根Fragment

    public BaseActivity() {
        try {
            if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
                Class<P> pClass = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
                Constructor[] constructors = pClass.getDeclaredConstructors();
                constructors[0].setAccessible(true);
                mPresenter = pClass.newInstance();
                mPresenter.attachView(this);
            }
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置输入框模式
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        super.onCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachContext(this);
        }
        ActivityUtil.getAppManager().addActivity(this);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        ImmersionBar.setTitleBar(this, this.findViewById(R.id.toolbar));
        if (setRootFragment() != null) {
            setContentView(R.layout.activity_long_layout);
            loadRootFragment(R.id.long_container, setRootFragment());
        } else {
            if (getResId() instanceof Integer) {
                setContentView((Integer) getResId());
            } else if (getResId() instanceof View) {
                mView = (View) getResId();
                setContentView(mView);
            } else {
                throw new RuntimeException("getResId() must Integer or View");
            }
        }
        if (mView == null) {
            mView = findViewById(android.R.id.content);
        }

        if (isRxBus()) {
            RxBus.getInstance().toObservable(CommonEvent.class).subscribe(new Consumer<CommonEvent>() {
                @Override
                public void accept(CommonEvent commonEvent) throws Exception {
                    handleEvent(commonEvent);
                }
            });
        }

        onInitView(savedInstanceState);
        onInitData(savedInstanceState);
        if (isDelayed()) {
            mView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onLoadData();
                }
            }, 500);
        } else {
            onLoadData();
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
        cancleToast();
        ActivityUtil.getAppManager().removeActivity(this);
        System.gc();
        System.runFinalization();
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    @Override
    public void onBackPressedSupport() {
        onStateNotSaved();
        super.onBackPressedSupport();
    }

    //2.0以前使用 2.0以后被onBackPressed()替换。
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * Activity被系统杀死时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死.
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态.
     * 在onPause之前被调用.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Activity被系统杀死后再重建时被调用.
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity.
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 发送提示消息
     *
     * @param message
     */
    public void showToast(String message) {
        if (!TextUtils.isEmpty(message))
            return;
        if (mToast == null) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

    /**
     * 取消Toast提示
     */
    protected void cancleToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 显示输入法
     *
     * @param editText
     */
    protected void showSoftInput(EditText editText) {
        editText.requestFocus();
        editText.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏输入法
     */
    protected void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getWindow().peekDecorView();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new FragmentAnimator(R.anim.popenter_anim, R.anim.popexit_anim, R.anim.enter_anim, R.anim.exit_anim);
    }

    @Override
    public boolean isDelayed() {
        return true;
    }

    @Override
    public boolean isRxBus() {
        return false;
    }

    /**
     * eventbus事件回调方法
     *
     * @param event
     */
    public void handleEvent(CommonEvent event) {

    }

    public final void postEvent(CommonEvent event) {
        RxBus.getInstance().post(event);
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onLoadData() {

    }

    @Override
    public Object getResId() {
        return null;
    }

    public void showDialog() {
        if (dialog == null) {
            dialog = new HttpResultDialog(this);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing() && mPresenter.getDisposableNumber() == 0) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
