package com.like.base.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * 加载各种不同布局
 * 备注：主要是用于加载数据错误，无数据等布局
 * Created by longshao on 2017/5/17.
 */

public class LoadAnyLinearLayout extends LinearLayout {

    private Context mContext;
    private LinkedHashSet<View> viewLinkedList;

    public LoadAnyLinearLayout(Context context) {
        this(context, null);
    }

    public LoadAnyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadAnyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        viewLinkedList = new LinkedHashSet<>();
    }

    /**
     * 加载其他的布局布局
     *
     * @param resId
     */
    public void addOtherLayout(Integer resId) {
        int countViews = this.getChildCount();
        for (int i = 0; i < countViews; i++) {
            this.getChildAt(i).setVisibility(GONE);
        }
        if (viewLinkedList.size() > 0) {
            for (Iterator it = viewLinkedList.iterator(); it.hasNext(); ) {
                this.removeView((View) it.next());
            }
        }
        View childView = LayoutInflater.from(mContext).inflate(resId, null, false);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        childView.setLayoutParams(params);
        this.addView(childView);
        viewLinkedList.add(childView);
    }

    /**
     * 还原原来布局
     */
    public void showNomalLayout() {
        if (viewLinkedList.size() == 0)
            return;
        if (viewLinkedList.size() > 0) {
            for (Iterator it = viewLinkedList.iterator(); it.hasNext(); ) {
                this.removeView((View) it.next());
            }
        }
        int countViews = this.getChildCount();
        for (int i = 0; i < countViews; i++) {
            this.getChildAt(i).setVisibility(VISIBLE);
        }
    }
}
