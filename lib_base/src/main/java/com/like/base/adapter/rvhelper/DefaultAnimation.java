package com.like.base.adapter.rvhelper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;


/**
 * 默认的Item进入的动画
 * item、淡入的动画效果
 */
public class DefaultAnimation implements IRecycleAnimation {

    private static final float DEFAULT_ALPHA_FROM = 0f;
    private final float mFrom;

    public DefaultAnimation() {
        this(DEFAULT_ALPHA_FROM);
    }

    public DefaultAnimation(float from) {
        mFrom = from;
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)};
    }
}
