package com.like.utilslib.screen;

import android.util.TypedValue;

import com.like.utilslib.UtilApp;

/**
 * 常用单位转换的辅助类
 *
 * @author longshao
 */
public class DensityUtil {
    /**
     * dp转px
     *
     * @param dpVal
     * @return
     */
    public static int dpTopx(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, UtilApp.getIntance().getApplicationContext().getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    public static int spTopx(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, UtilApp.getIntance().getApplicationContext().getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param pxVal
     * @return
     */
    public static float pxTodp(float pxVal) {
        final float scale = UtilApp.getIntance().getApplicationContext().getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static float pxTosp(float pxVal) {
        return (pxVal / UtilApp.getIntance().getApplicationContext().getResources().getDisplayMetrics().scaledDensity);
    }
}
