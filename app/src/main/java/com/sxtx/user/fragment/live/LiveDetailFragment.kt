package com.sxtx.user.fragment.live

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.like.base.base.BaseFragment
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.login.Api
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.sxtx.user.R
import com.sxtx.user.fragment.deposit.DepositType1Fragment
import com.sxtx.user.fragment.main.LiveFragment1
import com.sxtx.user.fragment.main.TaskFragment
import com.sxtx.user.inter.SampleListener
import com.sxtx.user.model.bean.LiveBean
import com.sxtx.user.mvp.presenter.live.LiveDetailPresenter
import com.sxtx.user.mvp.view.live.ILiveDetailView
import com.sxtx.user.util.GlideOptionUtil
import com.sxtx.user.util.NoFastClickUtils
import kotlinx.android.synthetic.main.fragment_live_detail.*

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class LiveDetailFragment : BaseFragment<LiveDetailPresenter>(), ILiveDetailView {


    var bean: LiveBean? = null
    var orientationUtils: OrientationUtils? = null

    var isPlay: Boolean = false
    var isPause: Boolean = false
    var freeTime = 0L


    companion object {
        const val KEY_LIVE_MSG = "live_msg"  //直播相关数据
        fun newIncetance(liveBean: LiveBean): LiveDetailFragment {
            val fragment = LiveDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_LIVE_MSG, liveBean)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getResId(): Any {
        return R.layout.fragment_live_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        bean = bundle?.getSerializable(KEY_LIVE_MSG) as LiveBean
    }


    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        ImmersionBar.setTitleBar(activity, ImmersionBar.getStatusBarHeight(this.activity!!), status_bar_view_group)
        resolveNormalVideoUI()
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        tv_name.text = bean?.name
        Glide.with(activity!!).load(bean?.bannerImg).apply(GlideOptionUtil.getOption()).into(img_pic)

    }


    /**
     * 充值回调
     */
    override fun getPayData(respons: Api.GetPayDataResponse) {
        start(DepositType1Fragment.newIncetance(respons.topYpType))
    }

    override fun uploadTime(respons: Api.WatichLiveResponse) {
    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        mPresenter.GetLiveFreeTimeReqeust()
    }

    /**
     * 獲取免費時間
     */
    override fun getFreeTime(respons: Api.GetLiveFreeTimeResponse) {
        freeTime = respons.time
        if (respons.isVip == 1) {
            tv_vip.visibility = View.GONE
        } else {
            if (bean?.isFree == 1){
                tv_vip.visibility = View.GONE
            }else{
                tv_vip.visibility = View.VISIBLE
            }
        }
        initVideoShow(bean?.isFree == 1, respons.isVip == 1, bean!!.address, true)
    }

    /**
     * 初始化播放器
     */
    fun initVideoShow(isFree: Boolean, isVip: Boolean, url: String, isAutoPlay: Boolean) {
        try {
            val gsyVideoOption = GSYVideoOptionBuilder()
            gsyVideoOption
                    .setIsTouchWiget(false)
                    .setRotateViewAuto(false)
                    .setLockLand(false)
                    .setShowFullAnimation(false)
                    .setNeedLockFull(true)
                    .setSeekRatio(1f)
                    .setUrl(url)
                    .setCacheWithPlay(false)
                    .setVideoTitle("")
                    .setStandardVideoAllCallBack(object : SampleListener() {
                        override fun onPrepared(url: String, vararg objects: Any) {
                            Debuger.printfError("***** onPrepared **** " + objects[0])
                            Debuger.printfError("***** onPrepared **** " + objects[1])
                            super.onPrepared(url, objects)
                            //开始播放了才能旋转和全屏
                            orientationUtils!!.isEnable = true
                            isPlay = true
                        }

                        override fun onEnterFullscreen(url: String, vararg objects: Any) {
                            super.onEnterFullscreen(url, objects)
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[0])//title
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[1])//当前全屏player
                        }

                        override fun onAutoComplete(url: String, vararg objects: Any) {
                            super.onAutoComplete(url, objects)
                        }

                        override fun onClickStartError(url: String, vararg objects: Any) {
                            super.onClickStartError(url, objects)
                        }

                        override fun onPlayError(url: String?, vararg objects: Any?) {
                            super.onPlayError(url, *objects)
                            LogUtil.loge("當前直播播放錯誤--------------------》》》")
                            if (detailPlayer != null) {
                                detailPlayer.showPlayError(true)
                            }
                        }

                        override fun onQuitFullscreen(url: String, vararg objects: Any) {
                            super.onQuitFullscreen(url, objects)
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[0])//title
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[1])//当前非全屏player
                            if (orientationUtils != null) {
                                orientationUtils!!.backToProtVideo()
                            }
                        }
                    }).setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
                        if (!isFree) {
                            if (!isVip) {
                                if (currentPosition >= freeTime) {
                                    if (detailPlayer != null) {
                                        detailPlayer.showVipHint(true)
                                        detailPlayer.onVideoPause()
                                        detailPlayer.showPauseView(false)
                                        isPause = true
                                    }
                                } else {
                                    if (detailPlayer != null) {
                                        detailPlayer.showVipHint(false)
                                    }
                                }
                            } else {
                                if (detailPlayer != null) {
                                    detailPlayer.showVipHint(false)
                                }
                            }
                        }
                    }.setLockClickListener { _, lock ->
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils!!.isEnable = !lock
                        }
                    }.build(detailPlayer)

            detailPlayer.fullscreenButton.setOnClickListener {
                //直接横屏
                orientationUtils!!.resolveByClick()
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(activity, true, true)
            }

            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL)

            detailPlayer.startPlayLogic()

            detailPlayer.tv_left?.setOnClickListener {
                start(TaskFragment.newInstance(true))
            }
            detailPlayer.tv_right?.setOnClickListener {
                mPresenter.getPayDataResponse()
            }
            //开通会员
            tv_vip.setOnClickListener {
                mPresenter.getPayDataResponse()
            }


            //返回按鈕
            back.setOnClickListener {
                if (!NoFastClickUtils.isFastClick()) {
                    if (orientationUtils != null) {
                        orientationUtils!!.backToProtVideo()
                    }
                    if (detailPlayer != null) {
                        mPresenter.uploadPlayTime(detailPlayer.currentPositionWhenPlaying.toLong())
                    }
                    StandardGSYVideoPlayer.backFromWindowFull(activity)
                    val parent = parentFragment as BaseFragment<*>
                    parent.pop()
                }

            }
        } catch (e: NullPointerException) {
            showToast("当前直播源异常")
        }

    }


    override fun onBackPressedSupport(): Boolean {
        if (orientationUtils != null) {
            orientationUtils!!.backToProtVideo()
        }
        if (detailPlayer != null) {
            mPresenter.uploadPlayTime(detailPlayer.currentPositionWhenPlaying.toLong())
        }
        if (StandardGSYVideoPlayer.backFromWindowFull(activity)) {
            return true
        }
        return super.onBackPressedSupport()

    }


    override fun onPause() {
        detailPlayer.onVideoPause()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        detailPlayer.onVideoResume()
        super.onResume()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            if (detailPlayer != null) {
                mPresenter.uploadPlayTime(detailPlayer.currentPositionWhenPlaying.toLong())
                detailPlayer.release()
            }
        }
        if (orientationUtils != null)
            orientationUtils!!.releaseListener()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(activity, newConfig, orientationUtils)
        }
    }


    private fun resolveNormalVideoUI() {
        //增加title
        detailPlayer.titleTextView.visibility = View.GONE
        detailPlayer.backButton.visibility = View.VISIBLE
    }

}