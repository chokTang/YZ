package com.like.utilslib.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 描述：文本输入辅助类，通过管理多个TextView或者EditText输入是否为空来启用或者禁用按钮的点击事件
 */
public final class TextInputHelper implements TextWatcher {

    private View mView; // 操作按钮的View
    private boolean isAlpha; // 是否禁用后设置半透明度

    private List<TextView> mViewSet; // TextView集合，子类也可以（EditText、TextView、Button）
    private List<String> mViewMatcher;//TextView对应的正则效验集合

    private boolean mEnabled = false;

    public static final String NO_NULL = "^[\\s\\S]*.*[^\\s][\\s\\S]*$";
    public static final String CARD = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";//身份证号
    public static final String PHONE = "^((13[0-9])|(19[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
    public static final String EMAIL = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
    public static final String MAX_INTNTER = "^\\d{4}$";//只能输入4位的数字 可改4
    public static final String MAX_CHAR = "^.{6}$";//只能输入6未的字符串 可改6
    public static final String PASSWORD = "\\w{6,12}$";//正确格式为：以字母开头，长度在6~12之间，只能包含字符、数字和下划线。

    public static final String MAX_INTNTER_ALL = "^\\d{%d}$";//需要使用foter表单来写具体多少个长度的数字
    public static final String MAX_CHAR_ALL = "^.{%d}$";//需要使用foter表单来写具体多少个长度的字符串

    public TextInputHelper(View view) {
        this(view, true);
    }

    /**
     * 构造函数
     *
     * @param view  跟随EditText或者TextView输入为空来判断启动或者禁用这个View
     * @param alpha 是否需要设置透明度
     */
    public TextInputHelper(View view, boolean alpha) {
        if (view == null) throw new IllegalArgumentException("The view is empty");
        mView = view;
        isAlpha = alpha;
    }

    /**
     * 添加EditText或者TextView监听
     *
     * @param views 传入单个或者多个EditText或者TextView对象
     */
    public void addViews(TextView... views) {
        if (views == null) return;

        if (mViewSet == null) {
            mViewSet = new ArrayList<>(views.length - 1);
        }

        if (mViewMatcher == null) {
            mViewMatcher = new ArrayList<String>(views.length - 1);
        }

        for (TextView view : views) {
            view.addTextChangedListener(this);
            mViewSet.add(view);
            mViewMatcher.add(NO_NULL);
        }
        //初始化一下默认值
        afterTextChanged(null);
    }

    public TextInputHelper addView(TextView textView, String matcher) {
        if (mViewSet == null) {
            mViewSet = new ArrayList<>();
        }

        if (mViewMatcher == null) {
            mViewMatcher = new ArrayList<String>();
        }

        mViewSet.add(textView);
        mViewMatcher.add(matcher);
        return this;
    }

    /**
     * 必须使用 addView 添加的然后要执行此方法
     */
    public void addTextWatcher() {
        for (TextView view : mViewSet) {
            view.addTextChangedListener(this);
        }
        //初始化一下默认值
        afterTextChanged(null);
    }

    /**
     * 移除EditText监听，避免内存泄露
     * 备注：记得在不使用了，在onDestroy（）中移除
     */
    public void removeViews() {
        if (mViewSet == null) return;

        for (TextView view : mViewSet) {
            if (view != null) {
                view.removeTextChangedListener(this);
            }
        }
        mViewSet.clear();
        mViewSet = null;
    }

    /**
     * {@link TextWatcher}
     */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public synchronized void afterTextChanged(Editable s) {
        if (mViewSet == null) return;

        for (int i = 0; i < mViewSet.size(); i++) {
            if (!Pattern.matches(mViewMatcher.get(i), mViewSet.get(i).getText().toString())) {
                setEnabled(false);
                return;
            }
        }
        setEnabled(true);
    }

    /**
     * 设置View的事件
     *
     * @param enabled 启用或者禁用View的事件
     */
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;

        if (enabled == mView.isEnabled()) return;

        if (mOnEnabledChangBeforeListener != null) {
            if (!mOnEnabledChangBeforeListener.onEnabledChangeBefore(enabled)) {
                doSetEnabled(enabled);
            }
        } else {
            doSetEnabled(enabled);
        }
    }

    private void doSetEnabled(boolean enabled) {
        if (enabled) {
            //启用View的事件
            mView.setEnabled(true);
            if (isAlpha) {
                //设置不透明
                mView.setAlpha(1f);
            }
        } else {
            //禁用View的事件
            mView.setEnabled(false);
            if (isAlpha) {
                //设置半透明
                mView.setAlpha(0.5f);
            }
        }
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public interface OnEnabledChangBeforeListener {

        /**
         * 按钮enabled变化前回调
         *
         * @param enabled 启用或者禁用View的事件
         * @return 返回true表示拦截enabled的设置，反之不拦截
         */
        boolean onEnabledChangeBefore(boolean enabled);
    }

    private OnEnabledChangBeforeListener mOnEnabledChangBeforeListener;

    /**
     * 设置按钮enabled变化前监听
     *
     * @param listener 按钮enabled变化前监听器
     */
    public void setOnEnabledChangBeforeListener(OnEnabledChangBeforeListener listener) {
        mOnEnabledChangBeforeListener = listener;
    }
}