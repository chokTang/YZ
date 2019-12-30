package com.sxtx.user.util.icon;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconFontDescriptor;

/**
 * 自定义的字体图片类
 * Created by Administrator on 2017/7/22.
 */

public class FontLongModule implements IconFontDescriptor {

    @Override
    public String ttfFileName() {
        return "iconfont.ttf";
    }

    @Override
    public Icon[] characters() {
        return LongIcon.values();
    }
}
