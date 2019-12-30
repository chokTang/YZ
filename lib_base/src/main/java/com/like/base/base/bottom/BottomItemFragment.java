package com.like.base.base.bottom;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.like.base.base.BaseStatusFragment;
import com.like.base.base.presenter.BasePresenter;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 基础的Fragment
 * Created by longshao on 2017/8/16.
 */

public abstract class BottomItemFragment<P extends BasePresenter> extends BaseStatusFragment<P> {

    protected BaseBottomFragment parentFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentFragment = (BaseBottomFragment) getParentFragment();
    }

    @Override
    public void start(ISupportFragment toFragment) {
        parentFragment.start(toFragment);
    }

    @Override
    public void start(ISupportFragment toFragment, int launchMode) {
        parentFragment.start(toFragment, launchMode);
    }

    @Override
    public void startForResult(ISupportFragment toFragment, int requestCode) {
        parentFragment.startForResult(toFragment, requestCode);
    }
}
