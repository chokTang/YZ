package com.like.base.net.conver;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by :longshao
 * Date: 2019/3/13  11:04
 * Desc:
 */
public class BodyRequestBodyConverter<T> implements Converter<T, RequestBody> {
    static final BodyRequestBodyConverter<Object> INSTANCE = new BodyRequestBodyConverter<>();
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain; charset=UTF-8");

    private BodyRequestBodyConverter() {
    }

    @Override public RequestBody convert(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, String.valueOf(value));
    }
}