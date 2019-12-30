package com.like.base.base.bottom;

import android.support.annotation.DrawableRes;

/**
 * 底部按钮实体
 * Created by longshao on 2017/8/16.
 */

public class BottomTabBean {
    private final CharSequence TITLE;
    @DrawableRes
    private final int NOMALDRAWBELRES;
    @DrawableRes
    private final int SELECTDRAWBELRES;

    public BottomTabBean(CharSequence title, int nomalDrawbelres, int selectDrawbelres) {
        this.TITLE = title;
        this.NOMALDRAWBELRES = nomalDrawbelres;
        this.SELECTDRAWBELRES = selectDrawbelres;
    }

    public CharSequence getTitle() {
        return TITLE;
    }

    public int getNomalDrawbelres() {
        return NOMALDRAWBELRES;
    }

    public int getSelectDrawbelres() {
        return SELECTDRAWBELRES;
    }
}
