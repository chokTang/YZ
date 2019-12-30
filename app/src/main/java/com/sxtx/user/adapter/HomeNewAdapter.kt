package com.sxtx.user.adapter

import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.util.LruCache
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.like.utilslib.app.CommonUtil
import com.lyh.protocol.data.PublicData
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.sxtx.user.ConfigData
import com.sxtx.user.R
import com.sxtx.user.util.DecryptFile
import com.sxtx.user.util.GlideImageLoader
import com.sxtx.user.util.GlideUtils
import com.sxtx.user.util.ImageUrlRequestUtil
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.MalformedURLException
import java.util.concurrent.TimeUnit

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class HomeNewAdapter : BaseQuickAdapter<PublicData.NewestVideoData, BaseViewHolder>(R.layout.item_home_new) {

    var type = 1 //1顯示下面bottom  2 不顯示bottom
    var position = -1
    var isColle = 1
    var mLruCache: LruCache<String, SVGAVideoEntity>? = null
    var mTotalSize = Runtime.getRuntime().totalMemory().toInt()

    override fun convert(helper: BaseViewHolder?, item: PublicData.NewestVideoData?) {
        item?.run {
            GlideUtils.GlideUtil(pictureAddress, helper!!.getView<ImageView>(R.id.img_pic))
            helper.setText(R.id.tv_name, videoName)
            helper.setText(R.id.tv_time, upTime)
            helper.setText(R.id.tv_length, videoTime)
            if (TextUtils.isEmpty(playNum)) helper.setText(R.id.tv_watch, "0") else helper.setText(R.id.tv_watch, playNum)
            if (TextUtils.isEmpty(giveMark)) helper.setText(R.id.tv_star, "0") else helper.setText(R.id.tv_star, giveMark)
            if (helper.adapterPosition == position) {
                helper.getView<ImageView>(R.id.img_collection)!!.isSelected = isColle == 1
            } else {
                helper.getView<ImageView>(R.id.img_collection)!!.isSelected = isCollect == 1
            }
            helper.addOnClickListener(R.id.img_collection)
            if (item.openSvga == 1) {
                helper.setGone(R.id.img_pic, false)
                helper.setGone(R.id.svga, true)
                mLruCache = LruCache(mTotalSize / 3)
                val entity = mLruCache!!.get(svgeAdress)
                if (entity != null) {
                    helper.getView<SVGAImageView>(R.id.svga).setVideoItem(entity)
                    helper.getView<SVGAImageView>(R.id.svga).startAnimation()
                } else {
                    try {
                        val svgaParser: SVGAParser? = SVGAParser(mContext)
                        //1.初始化OKHttp
                        val okHttpClient = OkHttpClient()
                                .newBuilder()
                                .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
                                .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                                .build()
                        //2.构建request
                        val request = Request.Builder()
                                .url(item.svgeAdress)
                                .build()
                        ImageUrlRequestUtil.singleExecutorService.execute {
                            val call = okHttpClient.newCall(request)
                            //4.接收响应（请求执行，并得到响应结果）
                            val execute = call.execute()
                            svgaParser!!.decodeFromInputStream(DecryptFile.getInPustream(execute.body()!!.byteStream(), ConfigData.DECODE_IMG_KEY), item.svgeAdress, object : SVGAParser.ParseCompletion {
                                override fun onComplete(videoItem: SVGAVideoEntity) {
                                    helper.getView<SVGAImageView>(R.id.svga).setVideoItem(videoItem)
                                    helper.getView<SVGAImageView>(R.id.svga).startAnimation()
                                    mLruCache!!.put(svgeAdress, videoItem)
                                }

                                override fun onError() {
                                }
                            }, true)
                        }
                    } catch (e: MalformedURLException) {
                        // new URL needs try catch.
                        e.printStackTrace()
                    }
                }
            } else {
                helper.setGone(R.id.img_pic, true)
                helper.setGone(R.id.svga, false)
            }
        }
        if (CommonUtil.isNull(item?.newestBannerList) || item?.newestBannerList!!.size == 0) {
            helper!!.setGone(R.id.fl_item, false)
        } else {
            helper!!.setGone(R.id.fl_item, true)
            helper.setText(R.id.tv_desc, item.newestBannerList[0].describe)
            val banner = helper.getView<Banner>(R.id.banner)
            val bannerlist: MutableList<String> = arrayListOf()
            banner.setImageLoader(GlideImageLoader())
            for (i in 0 until item.newestBannerList.size) {
                bannerlist.add(item.newestBannerList[i].pictureAddress)
            }
            //设置图片集合
            banner.setImages(bannerlist)
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            banner.start()
            banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    if (item.newestBannerList.size == 0)
                        return
                    if (item.newestBannerList.size > position) {
                        if (TextUtils.isEmpty(item.newestBannerList[position].describe)) {
                            helper.setGone(R.id.ll_desc, false)
                        } else {
                            helper.setGone(R.id.ll_desc, true)
                            if (position > 0) {
                                helper.setText(R.id.tv_desc, item.newestBannerList[position].describe)
                            }
                        }
                    }
                }

            })
            banner.setOnBannerListener { position -> bannerClick?.click(position, item.newestBannerList[position]) }
        }
    }


    interface BannerCLick {
        fun click(position: Int, bean: PublicData.AdvertisingData)
    }

    var bannerClick: BannerCLick? = null

    operator fun invoke(bannerClick: BannerCLick?) {
        this.bannerClick = bannerClick
    }
}