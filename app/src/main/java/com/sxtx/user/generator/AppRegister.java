package com.sxtx.user.generator;

import com.example.annotations.AppRegisterGenerator;
import com.like.base.wechat.template.AppRegisterTemplate;

/**
 * Created by longshao on 2017/8/14.
 */

@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = "com.sxtx.user",
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
