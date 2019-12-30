package com.sxtx.user.util.icon;

import com.joanzapata.iconify.Icon;

/**
 * 具体添加图片字体的地方
 * Created by Administrator on 2017/7/22.
 */

public enum LongIcon implements Icon {
    inon_scon('\ue62e'),
    icon_oli_pay('\ue61a');

    private char character;

    LongIcon(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}
