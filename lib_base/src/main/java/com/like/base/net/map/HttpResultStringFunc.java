package com.like.base.net.map;

import com.like.base.net.model.HttpResult;
import com.like.utilslib.json.JSONUtil;
import com.like.utilslib.other.LogUtil;

import org.json.JSONObject;

import io.reactivex.functions.Function;

/**
 * 数据拦截器
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 *
 * @param <String> Subscriber需要的数据类型，也就是返回中data数据类型
 *                 Created by longshao on 2017/3/20.
 */

public class HttpResultStringFunc<String> implements Function<String, String> {

    @Override
    public String apply(String tHttpResultFunc) {
        JSONObject resultObject = JSONUtil.toJsonObject((java.lang.String) tHttpResultFunc);
        int code = JSONUtil.get(resultObject, HttpResult.DATA_CODE, 0);
        LogUtil.loge("resultCode-----------" + code + "");
        if (code != HttpResultApiException.HTTP_CODE_SUCCESS) {
            java.lang.String message = JSONUtil.get(resultObject, HttpResult.DATA_MESSAGE, "");
            throw new HttpResultApiException(message);
        }
        return (String) JSONUtil.get(resultObject, HttpResult.DATA_KEY, "");
    }
}
