package com.sxtx.user.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.like.base.adapter.rvhelper.DividerItemDecoration
import com.like.base.base.BaseActivity
import com.like.base.base.BaseFragment
import com.like.utilslib.app.CommonUtil
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.sxtx.user.R
import com.sxtx.user.adapter.RecVideoAdapter
import com.sxtx.user.fragment.deposit.DepositType1Fragment
import com.sxtx.user.fragment.main.TaskFragment
import com.sxtx.user.inter.SampleListener
import com.sxtx.user.inter.VolumeChangeObserver
import com.sxtx.user.mvp.presenter.home.VideoPlayPresenter
import com.sxtx.user.mvp.view.home.IVideoPlayView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.GlideImageLoader
import com.sxtx.user.util.UrlCheckUtils
import com.sxtx.user.widget.VideoPlayer
import com.youth.banner.BannerConfig
import event.HeartServiceEvents
import kotlinx.android.synthetic.main.fragment_video_play.*
import kotlinx.android.synthetic.main.video_layout_normal.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class VideoPlayFragment : BaseFragment<VideoPlayPresenter>(), IVideoPlayView, View.OnClickListener, VolumeChangeObserver.VolumeChangeListener {

    var mvolume = 0
    override fun onVolumeChanged(volume: Int) {
        mvolume = volume
        AppPreference.getIntance().mute = volume == 0
        if (volume==0){
            detailPlayer.img_mute?.setBackgroundResource(R.drawable.icon_volu_1)
            fullPlayer?.img_mute?.setBackgroundResource(R.drawable.icon_volu_1)
        }else{
            detailPlayer.img_mute?.setBackgroundResource(R.drawable.icon_volu_2)
            fullPlayer?.img_mute?.setBackgroundResource(R.drawable.icon_volu_2)
        }
        GSYVideoManager.instance().isNeedMute = false
    }


    var mVolumeChangeObserver: VolumeChangeObserver? = null

    val images: MutableList<String> = arrayListOf()

    var adapter: RecVideoAdapter? = null
    var videoId = 0L
    var isShowDes = false
    var currentLenth = 0L
    var orientationUtils: OrientationUtils? = null

    var isPlay: Boolean = false
    var isPause: Boolean = false
    var notRetry: Boolean = false
    var bannerData: MutableList<PublicData.AdvertisingData> = arrayListOf()
    var videoUrl = ""
    var fullPlayer: VideoPlayer? = null
    var response1: Api.GetPlayVideoResponse? = null
    var mscreensize = 2

    companion object {
        const val KEY_VIDEO_ID = "video_id"
        fun newIncetance(videoId: Long): VideoPlayFragment {
            val fragment = VideoPlayFragment()
            val bundle = Bundle()
            bundle.putLong(KEY_VIDEO_ID, videoId)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getResId(): Any {
        return R.layout.fragment_video_play
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //实例化对象并设置监听器
        mVolumeChangeObserver = VolumeChangeObserver(activity)
        mVolumeChangeObserver?.volumeChangeListener = this
        val bundle = arguments
        if (bundle != null) {
            videoId = bundle.getLong(KEY_VIDEO_ID)
        }
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        resolveNormalVideoUI()
        ImmersionBar.setTitleBar(activity, ImmersionBar.getStatusBarHeight(activity!!), title)
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_rec.layoutManager = linearLayoutManager
        adapter = RecVideoAdapter()
        rv_rec.adapter = adapter
        rv_rec.addItemDecoration(DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, R.drawable.listdivider_white_16))
        banner.isFocusable = false
        rv_rec.isFocusable = false
        rv_rec.isNestedScrollingEnabled = false
        EventBus.getDefault().register(this)
        banner.setOnBannerListener { position ->
            if (bannerData[position].goWhere == 1) {
                if (UrlCheckUtils.checkUrlIsEffective(bannerData[position].externalAddress)) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(bannerData[position].externalAddress))))
                    mPresenter.clickAd(bannerData[position].id)
                } else
                    showToast("無效鏈接")

            } else {
                try {
                    val result = Class.forName(bannerData[position].androidAdress) as Class<*>
                    val ob = result.newInstance()
                    if (ob is BaseFragment<*>) {
                        val baseFragment = ob
                        start(baseFragment)
                    } else if (ob is BaseActivity<*>) {
                        val intent = Intent(activity, result)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (ll_desc == null || bannerData.size == 0)
                    return
                if (bannerData.size > position) {
                    if (TextUtils.isEmpty(bannerData[position].describe)) {
                        ll_desc?.visibility = View.GONE
                    } else {
                        ll_desc?.visibility = View.VISIBLE
                        tv_desc?.text = bannerData[position].describe
                    }
                }
            }
        })
        setListener()
    }


    fun setListener() {
        tv_like.setOnClickListener(this)
        tv_step_on.setOnClickListener(this)
        tv_score.setOnClickListener(this)
        tv_des.setOnClickListener(this)
        img_collection.setOnClickListener(this)
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)


        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as PublicData.ClassifyVideoData
            when (view.id) {
                R.id.ll_collection -> {
                    if (view.isSelected) {
                        mPresenter.deleteCollectRecrdReqeust(view, bean.videoId)
                    } else {
                        mPresenter.clickCollectReqeust(view, bean.videoId)
                    }
                }
            }
        }

        adapter?.setOnItemClickListener { adapter, _, position ->
            if (detailPlayer != null) {
                mPresenter.uploadPlayTime(detailPlayer.currentPositionWhenPlaying.toLong())
                val bean = adapter.data[position] as PublicData.ClassifyVideoData
                detailPlayer.release()
                GSYVideoManager.instance().releaseMediaPlayer()
                if (orientationUtils != null) {
                    orientationUtils!!.backToProtVideo()
                }
                AppPreference.getIntance().mute =false
                currentLenth = 0L
                detailPlayer.seekOnStart = 0
                detailPlayer.startPlayLogic()
                mPresenter.getpPlayVideoReqeust(bean.videoId)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun getVideoData(response: Api.GetPlayVideoResponse) {
        response1 = response
        bannerData = response.playVideoData.playBannerList
        initVideoShow(response.playVideoData.isVip == 1, response.playVideoData.videoPlayUrl, response.playVideoData.autoPlay == 1, response.playVideoData.screenSize)
        tv_name.text = response.playVideoData.videoName
        img_collection.isSelected = response.playVideoData.isCollect == 1
//        tv_time.text = "${AppCommonUtil.formatNum(response.playVideoData.videoPlayNum.toString(), true)}次播放   ${response.playVideoData.upTime}"
        tv_time.text = response.playVideoData.upTime
        if (TextUtils.isEmpty(response.playVideoData.intro)) {
            tv_des_content.text = "該視頻暫無簡介..."
        } else {
            tv_des_content.text = response.playVideoData.intro
        }
        tv_like.text = response.playVideoData.likeNumber
        tv_step_on.text = response.playVideoData.trampleNumber
        tv_score.text = response.playVideoData.giveMark
        tv_eye.text = response.playVideoData.videoPlayNum
        when (response.playVideoData.likeToTrample) {
            0 -> {
                tv_like.isSelected = false
                tv_step_on.isSelected = false
            }
            1 -> {
                tv_like.isSelected = true
                tv_step_on.isSelected = false
            }
            2 -> {
                tv_like.isSelected = false
                tv_step_on.isSelected = true
            }
        }

        if (CommonUtil.isNull(response.playVideoData.playBannerList) || response.playVideoData.playBannerList.size == 0) {
            ff_item.visibility = View.GONE
        } else {
            ff_item.visibility = View.VISIBLE
            images.clear()
            for (i in 0 until response.playVideoData.playBannerList.size) {
                images.add(response.playVideoData.playBannerList[i].pictureAddress)
            }
            banner.setImageLoader(GlideImageLoader())
            //设置图片集合
            banner.setImages(images)
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            banner.start()

        }
        adapter?.setNewData(response.playVideoData.videoDataList)

    }


    override fun collection(view: View, type: Int) {
        if (type == 1) {
            view.isSelected = true
        }
    }

    override fun cancleCollection(view: View) {
        view.isSelected = false
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_des -> {//简介
                isShowDes = !isShowDes
                var drawleft: Drawable? = null
                if (isShowDes) {
                    drawleft = resources.getDrawable(R.mipmap.icon_up)
                    tv_des_content.visibility = View.VISIBLE
                } else {
                    drawleft = resources.getDrawable(R.mipmap.icon_down)
                    tv_des_content.visibility = View.GONE
                }
                drawleft!!.setBounds(0, 0, drawleft.minimumWidth, drawleft.minimumHeight)
                tv_des!!.setCompoundDrawables(null, null, drawleft, null)
            }
            R.id.img_collection -> {

                if (v.isSelected) {
                    mPresenter.deleteCollectRecrdReqeust(v, videoId)
                } else {
                    mPresenter.clickCollectReqeust(v, videoId)
                }
            }
            R.id.tv_like -> {
                if (tv_like.isSelected) {
                    showToast("您已經點過讚了")
                } else {
                    mPresenter.likeVideo(videoId, 1)
                }
            }
            R.id.tv_step_on -> {
                if (tv_step_on.isSelected) {
                    showToast("您已經點過踩了")
                } else {
                    mPresenter.likeVideo(videoId, 2)
                }
            }
            R.id.tv_score -> {

            }
        }
    }


    override fun getPayData(respons: Api.GetPayDataResponse) {
        start(DepositType1Fragment.newIncetance(respons.topYpType))
    }


    /**
     * 初始化播放器
     */
    fun initVideoShow(isVip: Boolean, url: String, isAutoPlay: Boolean, screensize: Int) {
        try {
            //外部辅助的旋转，帮助全屏
            orientationUtils = OrientationUtils(activity, detailPlayer)
            //初始化不打开外部的旋转
            orientationUtils!!.isEnable = false
            mscreensize = screensize

            if (detailPlayer != null) {
                detailPlayer?.isVip = isVip
                detailPlayer?.showVip(!isVip)
                detailPlayer.showVipHint(false)
                detailPlayer?.start?.visibility = View.VISIBLE
            }

            val gsyVideoOption = GSYVideoOptionBuilder()
            gsyVideoOption
                    .setEnlargeImageRes(R.drawable.icon_full_screen)
                    .setIsTouchWiget(true)
                    .setRotateViewAuto(false)
                    .setLockLand(false)
                    .setShowFullAnimation(false)
                    .setNeedLockFull(false)
                    .setSeekRatio(1f)
                    .setUrl(url)
                    .setCacheWithPlay(false)
                    .setThumbPlay(true)
                    .setVideoTitle("")
                    .setStandardVideoAllCallBack(object : SampleListener() {
                        override fun onPrepared(url: String, vararg objects: Any) {
                            Debuger.printfError("***** onPrepared **** " + objects[0])
                            Debuger.printfError("***** onPrepared **** " + objects[1])
                            videoUrl = url
                            super.onPrepared(url, objects)
                            //开始播放了才能旋转和全屏gf
                            orientationUtils!!.isEnable = true
                            isPlay = true
                        }

                        override fun onClickBlank(url: String?, vararg objects: Any?) {
                            val videoPlayer = objects[1] as VideoPlayer
                            videoPlayer.start?.visibility = View.GONE
                            super.onClickBlank(url, *objects)
                        }

                        override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
                            val videoPlayer = objects[1] as VideoPlayer
                            videoPlayer.start?.visibility = View.GONE
                            super.onClickBlankFullscreen(url, *objects)
                        }

                        override fun onClickStartThumb(url: String?, vararg objects: Any?) {
                            val videoPlayer = objects[1] as VideoPlayer
                            videoPlayer.start?.visibility = View.GONE
                            super.onClickStartThumb(url, *objects)
                        }

                        override fun onEnterFullscreen(url: String, vararg objects: Any) {
                            //这里是控制全屏中的充值vip按钮是否显示
                            val full = objects[1] as VideoPlayer
                            fullPlayer = full
                            full.isVip = isVip
                            full.showVip(!isVip)
                            full.isShowAllFullScreen(false)
                            super.onEnterFullscreen(url, objects)
                            if (TextUtils.isEmpty(videoUrl)) {
                                full.start?.visibility = View.VISIBLE
                            } else {
                                full.start?.visibility = View.GONE
                            }
                            if (AppPreference.getIntance().mute){
                                full.img_mute?.setBackgroundResource(R.drawable.icon_volu_1)
                            }else{
                                full.img_mute?.setBackgroundResource(R.drawable.icon_volu_2)
                            }
                            if (mscreensize==1){
                                orientationUtils?.isLand = 1
                                orientationUtils?.isEnable = false
                                orientationUtils?.isRotateWithSystem = false
                            }
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[0])//title
                            Debuger.printfError("***** onEnterFullscreen **** " + objects[1])//当前全屏player
                        }

                        override fun onQuitFullscreen(url: String, vararg objects: Any) {
                            super.onQuitFullscreen(url, objects)
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[0])//title
                            Debuger.printfError("***** onQuitFullscreen **** " + objects[1])//当前非全屏player
                            fullPlayer = null
                            val full = objects[1] as VideoPlayer
                            if (TextUtils.isEmpty(videoUrl)) {
                                full.start?.visibility = View.VISIBLE
                            } else {
                                full.start?.visibility = View.GONE
                            }
                            if (AppPreference.getIntance().mute){
                                full.img_mute?.setBackgroundResource(R.drawable.icon_volu_1)
                            }else{
                                full.img_mute?.setBackgroundResource(R.drawable.icon_volu_2)
                            }
                            if (!full.isNeedLockFull) {
                                full.isShowAllFullScreen(true)
                                if (orientationUtils != null) {
                                    if (orientationUtils?.isLand == 0) {
                                        orientationUtils?.isLand = 1
                                    }
                                    orientationUtils!!.backToProtVideo()
                                }
                            }

                        }

                        override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
                            super.onTouchScreenSeekPosition(url, *objects)
                            LogUtil.loge("當前你在拖動視頻")
                        }

                        override fun onAutoComplete(url: String, vararg objects: Any) {
                            super.onAutoComplete(url, objects)
                            val player = objects[1] as VideoPlayer
                            if (isVip) {
                                if (response1?.playVideoData!!.videoDataList.size > 0) {
                                    player.ll_video_complete.visibility = View.VISIBLE
                                    mPresenter.uploadPlayTime(player.duration.toLong())
                                    player.showCompleteView(response1?.playVideoData!!.videoDataList[0])
                                } else {
                                    player.ll_video_complete.visibility = View.GONE
                                }
                            }

                        }

                        override fun onClickStartError(url: String, vararg objects: Any) {
                            super.onClickStartError(url, objects)
                        }

                        override fun onPlayError(url: String?, vararg objects: Any?) {
                            super.onPlayError(url, *objects)
                            LogUtil.loge("當前直播播放錯誤--------------------》》》")
//                        if (detailPlayer != null) {
//                            detailPlayer.showPlayError(true)
//                        }
                        }


                    }).setGSYVideoProgressListener { progress, secProgress, currentPosition, duration ->
                        if (duration / 1000 - currentPosition / 1000 < 2) {//这里判断结束的时候是 总时间减去当前时间在1S内为结束
                            if (detailPlayer != null) {
                                if (detailPlayer.isIfCurrentIsFullscreen) {
                                    detailPlayer.onBackFullscreen()
                                }
                                if (orientationUtils != null) {
                                    orientationUtils!!.isRotateWithSystem = false
                                }
                            }
                        }
                        if (!isVip) {
                            if (currentPosition >= 30000) {//非VIP大于等于30s要弹出VIP提示
                                if (detailPlayer != null) {
                                    detailPlayer?.showVip(false)
                                    detailPlayer.showVipHint(true)
                                    detailPlayer.onVideoPause()
                                    detailPlayer.showPauseView(false)
                                    if (detailPlayer.isIfCurrentIsFullscreen) {
                                        detailPlayer.onBackFullscreen()
                                    }
                                    isPause = true
                                }
                            } else {
                                if (duration < 31000) {//总时长小于31S 剪切了的然后播放完会弹出
                                    if (duration / 1000 - currentPosition / 1000 < 2) {//放总时长减去当前时长小于2S的时候我们认为即将播放完了 显示VIP提示
                                        if (detailPlayer != null) {
                                            detailPlayer.showVipHint(true)
//                                            detailPlayer.showVip(false)
                                        }
                                    }
                                } else {
                                    if (detailPlayer != null) {
                                        detailPlayer.showVipHint(false)
                                    }
                                }

                            }
                        } else {
                            if (detailPlayer != null) {
                                detailPlayer.showVipHint(false)
                            }
                        }
                    }.setLockClickListener { _, lock ->
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils!!.isEnable = !lock
                        }
                    }.build(detailPlayer)


            detailPlayer.fullscreenButton.setOnClickListener {
                LogUtil.loge("screensize " + screensize)
                if (orientationUtils!!.isEnable && isPlay) {
                    if (screensize != 1) {//宽频
                        //直接横屏
                        orientationUtils!!.resolveByClick()
                    }
                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    detailPlayer.startWindowFullscreen(activity, true, true)

                }
            }
            if (currentLenth != 0L) {//这是别的页面返回播放页面的时候 按已经播放的长度播放
                detailPlayer.seekOnStart = currentLenth
                detailPlayer.startPlayLogic()
            } else {
                if (AppPreference.getIntance().mute) {//开始播放时候若是静音就显示 并且静音
                    detailPlayer.img_mute?.setBackgroundResource(R.drawable.icon_volu_1)
                    GSYVideoManager.instance().isNeedMute = true
                }else{
                    detailPlayer.img_mute?.setBackgroundResource(R.drawable.icon_volu_2)
                    GSYVideoManager.instance().isNeedMute = false
                }
                if (isAutoPlay) {
                    detailPlayer.startPlayLogic()
                } else {
                    detailPlayer.seekOnStart = currentLenth
                }
            }

            detailPlayer.tv_left?.setOnClickListener {
                if (orientationUtils != null) {
                    orientationUtils!!.isRotateWithSystem = false
                }
                start(TaskFragment.newInstance(true))
            }
            detailPlayer.tv_right?.setOnClickListener {
                if (orientationUtils != null) {
                    orientationUtils!!.isRotateWithSystem = false
                }
                mPresenter.getPayDataResponse()
            }

            //返回按鈕
            detailPlayer.backButton.setOnClickListener {
                if (orientationUtils != null) {
                    orientationUtils!!.isEnable = false
                    orientationUtils!!.isRotateWithSystem = false
                }
                if (detailPlayer != null) {
                    mPresenter.uploadPlayTime(detailPlayer.currentPositionWhenPlaying.toLong())
                }
                GSYVideoManager.instance().setListener(null)
                GSYVideoManager.instance().setLastListener(null)
                StandardGSYVideoPlayer.backFromWindowFull(activity)
                pop()
            }
        } catch (e: Exception) {
            showToast("当前视频源异常")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Click(message: HeartServiceEvents) {
        when (message.eventId) {
            HeartServiceEvents.TO_CLICK_VIP -> {
                if (detailPlayer.isIfCurrentIsFullscreen) {
                    detailPlayer.onBackFullscreen()
                }
                mPresenter.getPayDataResponse()
            }
            HeartServiceEvents.VIDEO_NOT_RETRY -> {
                notRetry = true
            }

            HeartServiceEvents.TO_CLICK_PALY_NEXT -> {//播放下一个视频
                if (detailPlayer != null) {
                    detailPlayer.ll_video_complete.visibility = View.GONE
                    val bean = response1?.playVideoData!!.videoDataList[0]
                    detailPlayer.release()
                    if (orientationUtils != null) {
                        orientationUtils!!.backToProtVideo()
                    }
                    currentLenth = 0L
                    detailPlayer.seekOnStart = 0
                    detailPlayer.startPlayLogic()
                    mPresenter.getpPlayVideoReqeust(bean.videoId)
                }
            }
        }

    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        if (!notRetry)
            mPresenter.getpPlayVideoReqeust(videoId)
    }


    override fun onSupportInvisible() {
        super.onSupportInvisible()
        detailPlayer.onVideoPause()
        isPause = true
        currentLenth = if (detailPlayer.currentPositionWhenPlaying.toLong() > 30000) {
            30000
        } else {
            detailPlayer.currentPositionWhenPlaying.toLong()
        }
    }


    override fun onBackPressedSupport(): Boolean {
        if (orientationUtils != null) {
            orientationUtils!!.isEnable = false
            orientationUtils!!.isRotateWithSystem = false
        }
        if (detailPlayer != null) {
            mPresenter.uploadPlayTime(detailPlayer.currentPositionWhenPlaying.toLong())
        }
        GSYVideoManager.instance().setListener(null)
        GSYVideoManager.instance().setLastListener(null)
        StandardGSYVideoPlayer.backFromWindowFull(activity)
        return super.onBackPressedSupport()
    }


    override fun onPause() {
        detailPlayer.onVideoPause()
        //解注册广播接收器
        mVolumeChangeObserver?.unregisterReceiver()
        super.onPause()
        isPause = true
    }

    override fun onResume() {
        detailPlayer.onVideoResume()
        //注册广播接收器
        mVolumeChangeObserver?.registerReceiver()
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
        if (orientationUtils != null) {
            orientationUtils!!.isRotateWithSystem = true
            orientationUtils!!.backToProtVideo()
            orientationUtils!!.releaseListener()
        }
        GSYVideoManager.instance().setListener(null)
        GSYVideoManager.instance().setLastListener(null)
        EventBus.getDefault().unregister(this)
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


    /**
     * 上传时间回调
     */
    override fun uploadTime(respons: Api.WatichVideoResponse) {

    }


    /**
     * 点赞视频回调
     */
    override fun likeVideo(respons: Api.LikeToTrampleResponse) {
        tv_like.text = respons.likeNumber
        tv_step_on.text = respons.trampleNumber
        tv_score.text = respons.giveMark
        when (respons.likeToTrample) {
            0 -> {
                tv_like.isSelected = false
                tv_step_on.isSelected = false
            }
            1 -> {
                tv_like.isSelected = true
                tv_step_on.isSelected = false
            }
            2 -> {
                tv_like.isSelected = false
                tv_step_on.isSelected = true
            }
        }
    }

}