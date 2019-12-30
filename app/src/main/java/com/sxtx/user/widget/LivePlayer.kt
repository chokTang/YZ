package com.sxtx.user.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.shuyu.gsyvideoplayer.R
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import kotlinx.android.synthetic.main.live_layout_normal.view.*

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class LivePlayer : StandardGSYVideoPlayer {

    var errorLinearLayout: LinearLayout? = null
    var show_vip_hint: LinearLayout? = null
    var layout_bottom: LinearLayout? = null
    var ll_bottom: LinearLayout? = null
    var ll_start: LinearLayout? = null
    var layout_top: LinearLayout? = null
    var start: ImageView? = null
    var tv_left: TextView? = null
    var tv_right: TextView? = null
    private var mStartX: Int = 0
    private var mStartY: Int = 0
    private var mScaledTouchSlop: Int = 0

    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag!!) {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
    }

    override fun getLayoutId(): Int {
        return com.sxtx.user.R.layout.live_layout_normal
    }

    override fun init(context: Context?) {
        super.init(context)
        errorLinearLayout = findViewById(com.sxtx.user.R.id.error)
        layout_top = findViewById(com.sxtx.user.R.id.layout_top)
        show_vip_hint = findViewById(com.sxtx.user.R.id.show_vip_hint)
        start = findViewById(com.sxtx.user.R.id.start)
        ll_start = findViewById(com.sxtx.user.R.id.ll_start)
        ll_bottom = findViewById(com.sxtx.user.R.id.ll_bottom)
        layout_bottom = findViewById(com.sxtx.user.R.id.layout_bottom)
        tv_left = findViewById(com.sxtx.user.R.id.tv_left)
        tv_right = findViewById(com.sxtx.user.R.id.tv_right)
        ll_bottom?.visibility = View.GONE
        ll_start?.visibility = View.GONE
        surface_container.setOnClickListener {
            start?.visibility = View.GONE
            layout_bottom?.visibility = View.GONE
        }
    }


    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                GSYVideoView.CURRENT_STATE_PLAYING -> imageView.setImageResource(R.drawable.video_click_pause_selector)
                GSYVideoView.CURRENT_STATE_ERROR -> imageView.setImageResource(R.drawable.video_click_play_selector)
                else -> imageView.setImageResource(R.drawable.video_click_play_selector)
            }
        }
    }

    /**
     * 顯示播放錯誤頁面
     */
    fun showPlayError(isShow: Boolean) {
        if (isShow) {
            backButton.visibility = View.VISIBLE
            errorLinearLayout?.visibility = View.VISIBLE
            layout_top?.visibility = View.VISIBLE
        } else {
            backButton.visibility = View.GONE
            errorLinearLayout?.visibility = View.GONE
        }
    }

    /**
     * 顯示VIP頁面
     */
    fun showVipHint(isShow: Boolean) {
        if (isShow) {
            show_vip_hint?.visibility = View.VISIBLE
            start?.isClickable = false
            layout_bottom?.isClickable = false
        } else {
            start?.isClickable = true
            layout_bottom?.isClickable = true
            show_vip_hint?.visibility = View.GONE
        }
    }

    /**
     * 暫停的時候顯示的view
     */
    fun showPauseView(isShow: Boolean) {
        if (isShow) {
            layout_bottom?.visibility = View.VISIBLE
            start?.visibility = View.VISIBLE
        } else {
            layout_bottom?.visibility = View.GONE
            start?.visibility = View.GONE
        }
    }


    override fun touchDoubleUp() {
//        super.touchDoubleUp()
    }


    /**
     * 解决点击和VerticalViewPager滑动冲突问题
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x.toInt()
                mStartY = event.y.toInt()
                return true
            }
            MotionEvent.ACTION_UP -> {
                val endX = event.x.toInt()
                val endY = event.y.toInt()
                if (Math.abs(endX - mStartX) < mScaledTouchSlop && Math.abs(endY - mStartY) < mScaledTouchSlop) {
                    performClick()
                }
            }
        }
        return false
    }


}
