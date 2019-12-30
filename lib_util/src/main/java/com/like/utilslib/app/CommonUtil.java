package com.like.utilslib.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 公用的方法类 日常处理类
 * 提供:数据非空、类型强转、button防止多次点击
 * Created by longshao on 2017/5/9.
 */

public class CommonUtil {

    private static long lastClickTime = 0;
    private static long DIFF = 1000;
    private static int lastButtonId = -1;

    private static final String SEP1 = ",";

    /**
     * 判断是否非空
     *
     * @param obj
     * @return
     */
    public static boolean isNull(String obj) {
        if (TextUtils.isEmpty(obj) || obj.equals("null")) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Object obj) {
        if (obj == null || TextUtils.isEmpty(obj.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 类型强转化
     *
     * @param object
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T converToT(Object object, T defaultValue) {
        if (object == null || "".equals(object)) {
            return defaultValue;
        }
        Class<?> valueClass = defaultValue.getClass();
        if (valueClass == int.class || valueClass == Integer.class) {
            return (T) (Integer) Integer.parseInt(object.toString());
        }
        if (valueClass == double.class || valueClass == Double.class) {
            return (T) (Double) Double.parseDouble(object.toString());
        }
        if (valueClass == boolean.class || valueClass == Boolean.class) {
            return (T) (Boolean) Boolean.parseBoolean(object.toString());
        }
        if (valueClass == String.class) {
            return (T) object.toString();
        }
        return defaultValue;
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    if (i != list.size() - 1) {
                        sb.append(SEP1);
                    }
                } else {
                    sb.append(list.get(i));
                    if (i != list.size() - 1) {
                        sb.append(SEP1);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * String转换List
     *
     * @param listText :需要转换的文本
     * @return List<?>
     */
    public static List<String> StringToList(String listText) {
        if (listText == null || listText.equals("")) {
            return null;
        }
        List<String> list = new ArrayList<>();
        String[] text = listText.split(SEP1);
        for (String str : text) {
            list.add(str);
        }
        return list;
    }

    /**
     * 图片路径转化为路劲
     *
     * @param path
     * @return
     */
    public static String imageUrlToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    /**
     * 判断是否安装了某包名的APK
     *
     * @param context
     * @param packagename
     * @return
     */
    public static boolean checkMapAppsIsExist(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
