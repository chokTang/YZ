package com.like.base.net.interceptor;

import com.like.utilslib.other.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 打印log日志
 */
public class LogInterceptor extends BaseInterceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        MediaType mediaType = response.body().contentType();
//        String content = response.body().string();
        LogUtil.loge("请求地址为：| " + request.toString());
        printHeader(request.headers());
        printParams(request.body());
//        LogUtil.loge("请求体返回：| Response:" + content);

        LogUtil.loge("----------请求耗时:" + duration + "毫秒----------");
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, response.body().bytes())).build();
    }

    /**
     * 打印headers
     *
     * @param headers
     */
    private void printHeader(Headers headers) {
        if (headers == null) return;
        StringBuffer buffer = new StringBuffer();
        for (String name : headers.names()) {
            buffer.append(name + ":");
            buffer.append(headers.values(name).toString());
            buffer.append("|");
        }
        LogUtil.loge("请求头信息：| " + buffer.toString());
    }

    private void printParams(RequestBody body) {
        if (body == null) return;
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            String params = buffer.readString(charset);
            LogUtil.loge("请求参数： | " + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
