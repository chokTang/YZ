package com.sxtx.user.api;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 公共的serviceAPi接口
 *
 * @author longshao 2018年10月8日 14:18:01
 * // http://t.weather.sojson.com/api/weather/city/101030100 测试接口
 */
public interface ICommonApi {

    //加密登录
    @POST(".")
    Observable<ResponseBody> request(@Body RequestBody bytes);
}
