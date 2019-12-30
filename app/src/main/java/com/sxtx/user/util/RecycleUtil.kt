package com.sxtx.user.util

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator


/**
 *
 * 介紹:
 * 作者:CHOK
 */
class RecycleUtil {


    companion object {
        /**
         * @param recyclerView
         * 清除动画
         */
        fun cleanRecycleAnimator(recyclerView: RecyclerView) {
            // 第一种，直接取消动画
            val animator = recyclerView.itemAnimator as SimpleItemAnimator
            animator.supportsChangeAnimations = false
            animator.addDuration = 0
            animator.changeDuration = 0
            animator.moveDuration = 0
            animator.removeDuration = 0
        }
    }


}