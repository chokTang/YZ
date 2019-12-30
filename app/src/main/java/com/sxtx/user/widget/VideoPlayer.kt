package com.sxtx.user.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.media.AudioManager
import android.util.AttributeSet
import android.view.*
import android.widget.*
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.data.PublicData
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.R
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.GlideUtils
import event.HeartServiceEvents
import kotlinx.android.synthetic.main.video_end.view.*
import kotlinx.android.synthetic.main.video_layout_normal.view.*
import moe.codeest.enviews.ENDownloadView
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.math.RoundingMode

/**
 *
 * 介紹:
 * 作者:CHOK
 */
open class VideoPlayer : StandardGSYVideoPlayer {

    var errorLinearLayout: LinearLayout? = null
    var show_vip_hint: LinearLayout? = null
    var layout_bottom: LinearLayout? = null
    var layout_top: LinearLayout? = null
    var thumb: RelativeLayout? = null
    var start: ImageView? = null
    var start_bottom: ImageView? = null
    var img_mute: ImageView? = null
    var tv_left: TextView? = null
    var tv_right: TextView? = null
    var moveTime: Long? = null
    var isVip = false

    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag!!)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun getLayoutId(): Int {
        return com.sxtx.user.R.layout.video_layout_normal
    }

    override fun init(context: Context?) {
        super.init(context)
        errorLinearLayout = findViewById(com.sxtx.user.R.id.error)
        layout_top = findViewById(com.sxtx.user.R.id.layout_top)
        show_vip_hint = findViewById(com.sxtx.user.R.id.show_vip_hint)
        thumb = findViewById(com.sxtx.user.R.id.thumb)
        start = findViewById(com.sxtx.user.R.id.start_1)
        start_bottom = findViewById(com.sxtx.user.R.id.start)
        img_mute = findViewById(com.sxtx.user.R.id.img_mute)
        layout_bottom = findViewById(com.sxtx.user.R.id.layout_bottom)
        layout_bottom?.visibility = View.GONE
        tv_left = findViewById(com.sxtx.user.R.id.tv_left)
        tv_right = findViewById(com.sxtx.user.R.id.tv_right)
        tv_vip.setOnClickListener {
            EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.TO_CLICK_VIP, null))
        }

        start?.setOnClickListener {
            start?.visibility = View.GONE
            clickStartIcon()
        }
        //完成播放后的立即播放下一个视频
        tv_play.setOnClickListener {
            EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.TO_CLICK_PALY_NEXT, null))
        }

        //完成播放重播
        ll_replay.setOnClickListener {
            ll_video_complete.visibility = View.GONE
            this.clickStartIcon()
        }

        //静音就显示静音图标 否则就显示非静音图标
        if (AppPreference.getIntance().mute) {
            img_mute?.setBackgroundResource(com.sxtx.user.R.drawable.icon_volu_1)
        } else {
            img_mute?.setBackgroundResource(com.sxtx.user.R.drawable.icon_volu_2)
        }

        img_mute?.setOnClickListener {
            if (AppPreference.getIntance().mute) {//不是静音  就让它静音
                GSYVideoManager.instance().isNeedMute = false
                AppPreference.getIntance().mute = false
                img_mute?.setBackgroundResource(com.sxtx.user.R.drawable.icon_volu_2)
            } else {//是静音就不静音
                GSYVideoManager.instance().isNeedMute = true
                AppPreference.getIntance().mute = true
                img_mute?.setBackgroundResource(com.sxtx.user.R.drawable.icon_volu_1)
            }

        }

    }


    fun showCompleteView(videoData: PublicData.ClassifyVideoData) {
        GlideUtils.GlideUtil(videoData.pictureAddress, img_cover)
        tv_content.text = videoData.videoName
        tv_publish_time.text = videoData.upTime
    }


    override fun clickStartIcon() {
        start?.visibility = View.GONE
        super.clickStartIcon()
    }


    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                GSYVideoView.CURRENT_STATE_PLAYING -> {
                    imageView.setImageResource(com.sxtx.user.R.drawable.icon_video_pause)
//                    start?.setImageResource(R.drawable.video_click_pause_selector)
                }
                GSYVideoView.CURRENT_STATE_ERROR -> {
                    imageView.setImageResource(com.sxtx.user.R.drawable.icon_video_play)
//                    start?.setImageResource(R.drawable.video_click_play_selector)
                }
                else -> {
                    imageView.setImageResource(com.sxtx.user.R.drawable.icon_video_play)
//                    start?.setImageResource(R.drawable.video_click_play_selector)
                }
            }
        }
    }


    override fun changeUiToPreparingShow() {

        setViewShowState(mTopContainer, View.VISIBLE)
        setViewShowState(mBottomContainer, View.VISIBLE)
        setViewShowState(mStartButton, View.VISIBLE)
        setViewShowState(mLoadingProgressBar, View.VISIBLE)
        setViewShowState(mThumbImageViewLayout, View.INVISIBLE)
        setViewShowState(mBottomProgressBar, View.INVISIBLE)
        setViewShowState(mLockScreen, View.GONE)

        if (mLoadingProgressBar is ENDownloadView) {
            val enDownloadView = mLoadingProgressBar as ENDownloadView
            if (enDownloadView.currentState == ENDownloadView.STATE_PRE) {
                (mLoadingProgressBar as ENDownloadView).start()
            }
        }
    }


    override fun changeUiToPlayingBufferingShow() {

        setViewShowState(mTopContainer, View.VISIBLE)
        setViewShowState(mBottomContainer, View.VISIBLE)
        setViewShowState(mStartButton, View.VISIBLE)
        setViewShowState(mLoadingProgressBar, View.VISIBLE)
        setViewShowState(mThumbImageViewLayout, View.INVISIBLE)
        setViewShowState(mBottomProgressBar, View.INVISIBLE)
        setViewShowState(mLockScreen, View.GONE)

        if (mLoadingProgressBar is ENDownloadView) {
            val enDownloadView = mLoadingProgressBar as ENDownloadView
            if (enDownloadView.currentState == ENDownloadView.STATE_PRE) {
                (mLoadingProgressBar as ENDownloadView).start()
            }
        }
    }

    override fun changeUiToNormal() {
        setViewShowState(mTopContainer, View.VISIBLE)
        setViewShowState(mBottomContainer, View.INVISIBLE)
        setViewShowState(mStartButton, View.VISIBLE)
        setViewShowState(mLoadingProgressBar, View.INVISIBLE)
        setViewShowState(mThumbImageViewLayout, View.VISIBLE)
        setViewShowState(mBottomProgressBar, View.INVISIBLE)
        setViewShowState(mLockScreen, if (mIfCurrentIsFullscreen && mNeedLockFull) View.VISIBLE else View.GONE)

        updateStartImage()
        if (mLoadingProgressBar is ENDownloadView) {
            (mLoadingProgressBar as ENDownloadView).reset()
        }
    }

    override fun changeUiToCompleteShow() {
        setViewShowState(mTopContainer, View.VISIBLE)
        setViewShowState(mBottomContainer, View.GONE)
        setViewShowState(mStartButton, View.VISIBLE)
        setViewShowState(mLoadingProgressBar, View.INVISIBLE)
        setViewShowState(mThumbImageViewLayout, View.VISIBLE)
        setViewShowState(mBottomProgressBar, View.INVISIBLE)
        setViewShowState(mLockScreen, if (mIfCurrentIsFullscreen && mNeedLockFull) View.VISIBLE else View.GONE)
        if (mLoadingProgressBar is ENDownloadView) {
            (mLoadingProgressBar as ENDownloadView).reset()
        }
        updateStartImage()
    }

    override fun getProgressDialogLayoutId(): Int {
        return com.sxtx.user.R.layout.dialog_video_progress
    }

    /**
     * 触摸显示滑动进度dialog，如需要自定义继承重写即可，记得重写dismissProgressDialog
     */
    override fun showProgressDialog(deltaX: Float, seekTime: String, seekTimePosition: Int, totalTime: String, totalTimeDuration: Int) {
        if (mProgressDialog == null) {
            val localView = LayoutInflater.from(activityContext).inflate(progressDialogLayoutId, null)
            if (localView.findViewById<View>(progressDialogProgressId) is ProgressBar) {
                mDialogProgressBar = localView.findViewById<View>(progressDialogProgressId) as ProgressBar
                if (mDialogProgressBarDrawable != null) {
                    mDialogProgressBar.progressDrawable = mDialogProgressBarDrawable
                }
            }

            if (localView.findViewById<View>(progressDialogCurrentDurationTextId) is TextView) {
                mDialogSeekTime = localView.findViewById<View>(progressDialogCurrentDurationTextId) as TextView
            }
            if (localView.findViewById<View>(progressDialogAllDurationTextId) is TextView) {
                mDialogTotalTime = localView.findViewById<View>(progressDialogAllDurationTextId) as TextView
            }
            if (localView.findViewById<View>(progressDialogImageId) is ImageView) {
                mDialogIcon = localView.findViewById<View>(progressDialogImageId) as ImageView
            }
            mProgressDialog = Dialog(activityContext, com.sxtx.user.R.style.CommentStyle)
            mProgressDialog.setContentView(localView)
            mProgressDialog.window!!.addFlags(Window.FEATURE_ACTION_BAR)
            mProgressDialog.window!!.addFlags(32)
            mProgressDialog.window!!.addFlags(16)
            mProgressDialog.window!!.setLayout(width, height)
            if (mDialogProgressNormalColor != -11 && mDialogTotalTime != null) {
                mDialogTotalTime.setTextColor(mDialogProgressNormalColor)
            }
            if (mDialogProgressHighLightColor != -11 && mDialogSeekTime != null) {
                mDialogSeekTime.setTextColor(mDialogProgressHighLightColor)
            }
            val localLayoutParams = mProgressDialog.window!!.attributes
            localLayoutParams.gravity = Gravity.TOP
            localLayoutParams.width = width
            localLayoutParams.height = height
//            val location = IntArray(2)
//            getLocationOnScreen(location)
//            localLayoutParams.x = location[0]
//            localLayoutParams.y = location[1]
            mProgressDialog.window!!.attributes = localLayoutParams
        }
        if (!mProgressDialog.isShowing) {
            mProgressDialog.show()
        }
        if (mDialogSeekTime != null) {
            mDialogSeekTime.text = seekTime
        }
        if (mDialogTotalTime != null) {
            mDialogTotalTime.text = " / $totalTime"
        }
        if (totalTimeDuration > 0)
            if (mDialogProgressBar != null) {
                mDialogProgressBar.progress = seekTimePosition * 100 / totalTimeDuration
            }
        //隐藏快进快退按钮
//        if (deltaX > 0) {
//            if (mDialogIcon != null) {
//                mDialogIcon.setBackgroundResource(R.drawable.video_forward_icon)
//            }
//        } else {
//            if (mDialogIcon != null) {
//                mDialogIcon.setBackgroundResource(R.drawable.video_backward_icon)
//            }
//        }


    }

    override fun dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss()
            mProgressDialog = null
        }
    }


    override fun getVolumeLayoutId(): Int {
        return com.sxtx.user.R.layout.dialog_volume_video
    }

    override fun getVolumeProgressId(): Int {
        return R.id.volume_progressbar
    }

    /**
     * 声音dialog
     */
    var volumeBg: ImageView? = null

    override fun showVolumeDialog(deltaY: Float, volumePercent: Int) {
        if (volumePercent <= 0) {
            AppPreference.getIntance().mute = true
            img_mute?.setBackgroundResource(com.sxtx.user.R.drawable.icon_volu_1)
        } else {
            AppPreference.getIntance().mute = false
            img_mute?.setBackgroundResource(com.sxtx.user.R.drawable.icon_volu_2)
        }
        GSYVideoManager.instance().isNeedMute = false
        if (mVolumeDialog == null) {
            val localView = LayoutInflater.from(activityContext).inflate(volumeLayoutId, null)
            if (localView.findViewById<View>(volumeProgressId) is ProgressBar) {
                mDialogVolumeProgressBar = localView.findViewById<View>(volumeProgressId) as ProgressBar
                if (mVolumeProgressDrawable != null && mDialogVolumeProgressBar != null) {
                    mDialogVolumeProgressBar.progressDrawable = mVolumeProgressDrawable
                }
            }
            if (localView.findViewById<View>(com.sxtx.user.R.id.img_volume) is ImageView) {
                volumeBg = localView.findViewById<View>(com.sxtx.user.R.id.img_volume) as ImageView
            }
            mVolumeDialog = Dialog(activityContext, R.style.video_style_dialog_progress)
            mVolumeDialog.setContentView(localView)
            mVolumeDialog.window!!.addFlags(8)
            mVolumeDialog.window!!.addFlags(32)
            mVolumeDialog.window!!.addFlags(16)
//            mVolumeDialog.window!!.setLayout(-2, -2)
            mVolumeDialog.window!!.setLayout(width, height)
            val localLayoutParams = mVolumeDialog.window!!.attributes
            localLayoutParams.gravity = Gravity.TOP
            localLayoutParams.width = width
            localLayoutParams.height = height
//            val location = IntArray(2)
//            getLocationOnScreen(location)
//            localLayoutParams.x = location[0]
//            localLayoutParams.y = location[1]
            mVolumeDialog.window!!.attributes = localLayoutParams


        }
        if (!mVolumeDialog.isShowing) {
            mVolumeDialog.show()
        }
        if (mDialogVolumeProgressBar != null) {
            mDialogVolumeProgressBar.progress = volumePercent
        }
        if (volumeBg != null) {
            when {
                volumePercent <= 0 -> volumeBg!!.setImageResource(com.sxtx.user.R.drawable.icon_volu_1)
                volumePercent <= 33 -> volumeBg!!.setImageResource(com.sxtx.user.R.drawable.icon_volu_2)
                volumePercent <= 66 -> volumeBg!!.setImageResource(com.sxtx.user.R.drawable.icon_volu_3)
                else -> volumeBg!!.setImageResource(com.sxtx.user.R.drawable.icon_volu_4)
            }
        }

    }


    override fun dismissVolumeDialog() {
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss()
            mVolumeDialog = null
        }
    }


    override fun getBrightnessLayoutId(): Int {
        return com.sxtx.user.R.layout.dialog_bright_video
    }

    override fun getBrightnessTextId(): Int {
        return super.getBrightnessTextId()
    }

    /**
     * 亮度dialog
     */
    var brightness: ProgressBar? = null
    var roundProgressBar: RoundProgressBar? = null
    override fun showBrightnessDialog(percent: Float) {
        if (mBrightnessDialog == null) {
            val localView = LayoutInflater.from(activityContext).inflate(brightnessLayoutId, null)
            if (localView.findViewById<View>(com.sxtx.user.R.id.video_brightness) is ProgressBar) {
                brightness = localView.findViewById<View>(com.sxtx.user.R.id.video_brightness) as ProgressBar
            }
            if (localView.findViewById<View>(com.sxtx.user.R.id.roundProgressBar) is RoundProgressBar) {
                roundProgressBar = localView.findViewById<View>(com.sxtx.user.R.id.roundProgressBar) as RoundProgressBar
            }
            mBrightnessDialog = Dialog(activityContext, R.style.video_style_dialog_progress)
            mBrightnessDialog.setContentView(localView)
            mBrightnessDialog.window!!.addFlags(8)
            mBrightnessDialog.window!!.addFlags(32)
            mBrightnessDialog.window!!.addFlags(16)
            mBrightnessDialog.window!!.setLayout(-2, -2)
            val localLayoutParams = mBrightnessDialog.window!!.attributes
            localLayoutParams.gravity = Gravity.TOP or Gravity.RIGHT
            localLayoutParams.width = width
            localLayoutParams.height = height
            val location = IntArray(2)
            getLocationOnScreen(location)
            localLayoutParams.x = location[0]
            localLayoutParams.y = location[1]
            mBrightnessDialog.window!!.attributes = localLayoutParams
        }
        if (!mBrightnessDialog.isShowing) {
            mBrightnessDialog.show()
        }
        if (brightness != null) {
            brightness?.progress = (percent * 100).toInt()
        }
        if (roundProgressBar != null) {
            roundProgressBar?.progress = (percent * 100).toInt()
        }
//        if (mBrightnessDialogTv != null)
//            mBrightnessDialogTv.text = (percent * 100).toInt().toString() + "%"
    }

    override fun dismissBrightnessDialog() {
        if (mBrightnessDialog != null) {
            mBrightnessDialog.dismiss()
            mBrightnessDialog = null
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
            tv_vip?.visibility = View.GONE
            thumb?.isClickable = false
            surface_container?.isClickable = false
            layout_bottom?.isClickable = false
        } else {
            thumb?.isClickable = true
            surface_container?.isClickable = true
            layout_bottom?.isClickable = true
            show_vip_hint?.visibility = View.GONE
        }

    }


    /**
     * 是否顯示右上角的VIP充值提示
     */
    fun showVip(isShow: Boolean) {
        if (isShow) {
            tv_vip.visibility = View.VISIBLE
        } else {
            tv_vip.visibility = View.GONE
        }
    }

    /**
     * 全屏时候不显示 恢复全屏按钮  ，非全屏时候显示全屏按钮
     */

    fun isShowAllFullScreen(isShow: Boolean) {
        if (isShow) {
            fullscreen.visibility = View.VISIBLE
        } else {
            fullscreen.visibility = View.GONE
        }
    }

    /**
     * 暫停的時候顯示的view
     */
    fun showPauseView(isShow: Boolean) {
        if (isShow) {
            layout_bottom?.visibility = View.VISIBLE
        } else {
            layout_bottom?.visibility = View.GONE
        }
    }


    /**
     * 亮度、进度、音频
     */
    override fun onTouch(v: View, event: MotionEvent): Boolean {

        val id = v.id
        val x = event.x
        val y = event.y

        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            onClickUiToggle()
            startDismissControlViewTimer()
            return true
        }

        if (id == R.id.fullscreen) {
            return false
        }

        if (id == R.id.surface_container) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveTime = System.currentTimeMillis()
                    LogUtil.logd("--------moveTime start----$moveTime")
                    touchSurfaceDown(x, y)
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = x - mDownX
                    val deltaY = y - mDownY
                    val absDeltaX = Math.abs(deltaX)
                    val absDeltaY = Math.abs(deltaY)
                    if (isVip) {
                        if (mIfCurrentIsFullscreen && mIsTouchWigetFull || mIsTouchWiget && !mIfCurrentIsFullscreen) {
                            if (!mChangePosition && !mChangeVolume && !mBrightness) {
                                touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
                            }
                        }
                        touchSurfaceMove1(deltaX, deltaY, y)
                    }
                }
                MotionEvent.ACTION_UP -> {

                    startDismissControlViewTimer()

                    touchSurfaceUp()

                    startProgressTimer()

                    //不要和隐藏虚拟按键后，滑出虚拟按键冲突
                    if (mHideKey && mShowVKey) {
                        return true
                    }
                }
            }
            gestureDetector.onTouchEvent(event)
        } else if (id == R.id.progress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    cancelDismissControlViewTimer()
                    cancelProgressTimer()
                    var vpdown: ViewParent? = parent
                    while (vpdown != null) {
                        vpdown.requestDisallowInterceptTouchEvent(true)
                        vpdown = vpdown.parent
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    cancelProgressTimer()
                    var vpdown: ViewParent? = parent
                    while (vpdown != null) {
                        vpdown.requestDisallowInterceptTouchEvent(true)
                        vpdown = vpdown.parent
                    }
                }
                MotionEvent.ACTION_UP -> {
                    startDismissControlViewTimer()
                    startProgressTimer()
                    var vpup: ViewParent? = parent
                    while (vpup != null) {
                        vpup.requestDisallowInterceptTouchEvent(false)
                        vpup = vpup.parent
                    }
                    mBrightnessData = -1f
                }
            }
        }

        return false
    }


    private fun touchSurfaceMove1(deltaX: Float, deltaY: Float, y: Float) {

        var deltaY = deltaY
        val curWidth = if (CommonUtil.getCurrentScreenLand(activityContext as Activity)) mScreenHeight else mScreenWidth
        val curHeight = if (CommonUtil.getCurrentScreenLand(activityContext as Activity)) mScreenWidth else mScreenHeight
        if (mChangePosition) {
            //totalTimeDuration 视频总时长  deltaX 移动距离 curWidth 屏幕宽度（横屏为手机高度）
            val totalTimeDuration = duration
            val b1 = BigDecimal(deltaX.toDouble())
            val b2 = BigDecimal(curWidth)
            //视频滑动进度的比例系数
            mSeekRatio = b2.divide(b1, 2, RoundingMode.HALF_UP).toFloat()
            //滑动得到的时长
            var addPosition = (deltaX * totalTimeDuration / (curWidth * 3) / mSeekRatio).toInt()
            //进度条应显示的时间
            mSeekTimePosition = if (deltaX > 0) mDownPosition + addPosition else mDownPosition - addPosition
            //最长不能超过视频总时长
            if (mSeekTimePosition > totalTimeDuration)
                mSeekTimePosition = totalTimeDuration
            val seekTime = CommonUtil.stringForTime(mSeekTimePosition)
            val totalTime = CommonUtil.stringForTime(totalTimeDuration)
            showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration)
        } else if (mChangeVolume) {
            deltaY = -deltaY
            val max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val deltaV = (max.toFloat() * deltaY * 3f / curHeight).toInt()
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mGestureDownVolume + deltaV, 0)
            val volumePercent = (mGestureDownVolume * 100 / max + deltaY * 3f * 100f / curHeight).toInt()
            showVolumeDialog(-deltaY, volumePercent)
        } else if (!mChangePosition && mBrightness) {
            if (Math.abs(deltaY) > mThreshold) {
                val percent = -deltaY / curHeight
                onBrightnessSlide(percent)
                mDownY = y

            }
        }
    }

}
