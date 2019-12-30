package com.like.base.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.like.base.R;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created by longshao on 2017/5/17.
 */

public class AnyRefreshHeader extends ClassicsHeader {

    public AnyRefreshHeader(Context context) {
        super(context);
        initView(context);
    }

    public AnyRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AnyRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.setTextSizeTitle(14);//设置标题文字大小（sp单位）
        this.setTextSizeTime(12);//设置时间文字大小（sp单位）
        this.setAccentColor(ContextCompat.getColor(context, R.color.refreshColor));//设置强调颜色(包括字体，图片颜色)
//        this.setPrimaryColor(ContextCompat.getColor(context, R.color.red));//设置背景颜色
    }
}
