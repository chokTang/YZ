package com.sxtx.user.util;

import android.text.TextUtils;
import android.util.Patterns;

public class UrlCheckUtils {
    public static String checkUrl(String url){
        if (url.contains("http://")|| url.contains("https://")){
            return url;
        }else{
            if (checkUrlIsEffective("http://"+url))
                return "http://"+url;
            else if (checkUrlIsEffective("https://"+url))
                return "https://"+url;
        }
        return "";
    }

    public static boolean checkUrlIsEffective(String url){
        if (TextUtils.isEmpty(url)||!Patterns.WEB_URL.matcher(url).matches())
            return false;
        else
            return true;
    }
}
