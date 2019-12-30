package com.sxtx.user.api;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 用户相关API
 * Created by Administrator on 2017/12/30.
 */
public interface IAccountApi {

    //加密登录
    @POST(".")
    Observable<ResponseBody> login(@Body RequestBody bytes);

    //注册
    @FormUrlEncoded
    @POST("user/logreg/register")
    Observable<String> onRegister(@FieldMap Map<String, Object> params);


    //用户修改或者找回密码
    @FormUrlEncoded
    @POST("user/logreg/edit_password")
    Observable<String> onModifyPassword(@FieldMap Map<String, Object> params);
}
