package com.like.base.adapter.rvhelper;

import android.animation.Animator;
import android.view.View;

/**
 * item动画效果
 */
public interface IRecycleAnimation {
    public Animator[] getAnimators(View view);
}
