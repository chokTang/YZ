package com.like.base.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 自定义Gridview 重写其高度
 *
 * @author longshao
 */
public class AutoHeightRecycleView extends RecyclerView {

    public AutoHeightRecycleView(Context context) {
        super(context);

    }

    public AutoHeightRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
