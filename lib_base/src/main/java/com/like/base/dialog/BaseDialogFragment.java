package com.like.base.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.like.base.R;
import com.like.base.base.presenter.BasePresenter;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

/**
 * 基础的DialogFragmnt
 *
 * @author longshao 2018年9月5日 10:47:19
 */
public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment {

    @IntDef({FORM_TOP_TO_TOP, FORM_TOP_TO_BOTTOM, FORM_BOTTOM_TO_BOTTOM, FORM_BOTTOM_TO_TOP,
            FORM_RIGHT_TO_RIGHT, FORM_RIGHT_TO_LEFT, FORM_LEFT_TO_LEFT, FORM_LEFT_TO_RIGHT, CNTER, CENTER_DEFAULT})
    public @interface AnimationType {
    }

    public static final int FORM_TOP_TO_TOP = 0x200;
    public static final int FORM_TOP_TO_BOTTOM = 0x201;
    public static final int FORM_BOTTOM_TO_BOTTOM = 0x202;
    public static final int FORM_BOTTOM_TO_TOP = 0x203;
    public static final int FORM_RIGHT_TO_RIGHT = 0x204;
    public static final int FORM_RIGHT_TO_LEFT = 0x205;
    public static final int FORM_LEFT_TO_LEFT = 0x206;
    public static final int FORM_LEFT_TO_RIGHT = 0x207;
    public static final int CNTER = 0x208;
    public static final int CENTER_DEFAULT = 0x209;

    public abstract Object getResId();
    public abstract void initView();
    public abstract void initData();
    public abstract int getViewWidth();
    public abstract int getViewHeight();
    public abstract int getViewGravity();

    @AnimationType
    public abstract int getAnimationType();

    protected View mView;

    protected P mPresenter;

    public BaseDialogFragment() {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.BaseDialogFragmentStyle);
        if (mPresenter != null) {
            mPresenter.attachContext(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getResId() instanceof Integer) {
            mView = inflater.inflate((Integer) getResId(), container, false);
        } else if (getResId() instanceof View) {
            mView = (View) getResId();
        } else {
            throw new RuntimeException("getResId() must Integer or View");
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.windowAnimations=getAnimate();
        layoutParams.width=getViewWidth();
        layoutParams.height=getViewHeight();
        layoutParams.gravity=getViewGravity();
        window.setAttributes(layoutParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 初始化View
     *
     * @param resId
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int resId) {
        if (resId <= 0) {
            return (T) mView;
        } else {
            return (T) mView.findViewById(resId);
        }
    }

    /**
     * 获取dialog对话框弹出的样式
     * @return
     */
    @StyleRes
    private int getAnimate() {
        switch (getAnimationType()) {
            case FORM_BOTTOM_TO_BOTTOM:
                return R.style.ActionSheetDialogAnimation;
            case FORM_BOTTOM_TO_TOP:
                return R.style.FromBottomToTop;
            case FORM_TOP_TO_TOP:
                return R.style.FromTopToTop;
            case FORM_TOP_TO_BOTTOM:
                return R.style.FromTopToBottom;
            case FORM_LEFT_TO_LEFT:
                return R.style.FromLeftToLeft;
            case FORM_LEFT_TO_RIGHT:
                return R.style.FromLeftToRight;
            case FORM_RIGHT_TO_RIGHT:
                return R.style.FromRightToRight;
            case FORM_RIGHT_TO_LEFT:
                return R.style.FromRightToLeft;
            case CNTER:
                return R.style.centerIn;
            case CENTER_DEFAULT:
            default:
                return android.R.style.Animation;
        }
    }

    /**
     * 取消对话框
     */
    protected void onDailogDismiss(){
        getDialog().cancel();
    }

    /**
     * 点击物理按键，对话框是否消失
     * @param iskeyBack
     */
    public void setDailogKeyBack(final boolean iskeyBack){
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode== KeyEvent.KEYCODE_BACK||event.getAction()==KeyEvent.ACTION_DOWN){
                    if (!iskeyBack){
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void setCancelable(boolean iscancle){
        getDialog().setCanceledOnTouchOutside(iscancle);
    }
}
