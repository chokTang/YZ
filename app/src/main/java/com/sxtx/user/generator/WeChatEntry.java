package com.sxtx.user.generator;

import com.example.annotations.EntryGenerator;
import com.like.base.wechat.template.WXEntryTemplate;

/**
 * Created by longshao on 2017/8/14.
 */
@SuppressWarnings("unused")
@EntryGenerator(
        packageName = "com.sxtx.user",
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}
