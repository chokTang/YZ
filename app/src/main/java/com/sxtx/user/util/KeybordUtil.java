package com.sxtx.user.util;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class KeybordUtil {
    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public static void showInput(Fragment fragment,EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(fragment.getActivity()).getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    public static void hideInput(Fragment fragment) {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(fragment.getActivity()).getSystemService(INPUT_METHOD_SERVICE);
        View v = fragment.getActivity().getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
