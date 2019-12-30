package com.sxtx.user.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * 测试接口
 */
public interface TestApi {

    //https://www.sojson.com/open/api/weather/json.shtml?city=%E9%87%8D%E5%BA%86
    @GET()
    @Headers("Cache-Control:public, max-age=60")
    Observable<String> weatherRequest(@Url String url);
}
