package com.sxtx.user.generator;

import com.example.annotations.PayEntryGenerator;
import com.like.base.wechat.template.WXPayEntryTemplate;

/**
 * Created by longshao on 2017/8/14.
 */

@SuppressWarnings("unused")
@PayEntryGenerator(
        packageName = "com.sxtx.user",
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
