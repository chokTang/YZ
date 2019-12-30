package com.like.base.base.inter;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface IBaseActivity {
    void onInitView(@Nullable Bundle savedInstanceState);

    void onInitData(@Nullable Bundle savedInstanceState);

    void onLoadData();//请求网络数据

    Object getResId();

    boolean isDelayed();//是否延迟渲染数据

    boolean isRxBus();//是否使用eventbus
}
