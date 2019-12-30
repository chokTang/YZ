package com.like.base.base.bottom;

import java.util.LinkedHashMap;

/**
 * 底部按钮构造器集合
 * Created by longshao on 2017/8/16.
 */

public class ItemBuilder {

    private final LinkedHashMap<BottomTabBean,BottomItemFragment> ITEMS=new LinkedHashMap<>();

    static ItemBuilder builder(){
        return new ItemBuilder();
    }

    public final ItemBuilder addItem(BottomTabBean bean, BottomItemFragment delegate) {
        ITEMS.put(bean, delegate);
        return this;
    }

    public final ItemBuilder addItems(LinkedHashMap<BottomTabBean, BottomItemFragment> items) {
        ITEMS.putAll(items);
        return this;
    }

    public final LinkedHashMap<BottomTabBean, BottomItemFragment> build() {
        return ITEMS;
    }
}
