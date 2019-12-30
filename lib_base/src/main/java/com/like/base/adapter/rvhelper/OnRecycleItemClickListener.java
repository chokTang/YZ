package com.like.base.adapter.rvhelper;

import android.view.View;

import com.like.base.adapter.BaseRecyleAdapter;

/**
 * Created by AllenCoder on 2016/8/03.
 * <p>
 * <p>
 * A convenience class to extend when you only want to OnRecycleItemClickListener for a subset
 * of all the onRecycleItemTouchClickListener. This implements all methods in the
 * {@link onRecycleItemTouchClickListener}
 */
public abstract class OnRecycleItemClickListener extends onRecycleItemTouchClickListener {

    @Override
    public void onItemClick(BaseRecyleAdapter adapter, View view, int position) {
    }

    @Override
    public void onItemLongClick(BaseRecyleAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseRecyleAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseRecyleAdapter adapter, View view, int position) {

    }
}
