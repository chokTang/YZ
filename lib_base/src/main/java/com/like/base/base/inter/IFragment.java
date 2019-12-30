package com.like.base.base.inter;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by longshao on 2017/10/12.
 */

public interface IFragment {

    void onInitView(@Nullable Bundle savedInstanceState);

    void onInitData(@Nullable Bundle savedInstanceState);

    void onLoadData();//请求网络数据

    Object getResId();

    boolean isSwipeBack();//是否侧滑出站

    boolean isDelayed();//是否延迟渲染数据

    boolean isRxBus();//是否使用eventbus
}
