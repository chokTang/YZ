package com.sxtx.user.adapter

import android.text.TextUtils
import android.util.LruCache
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.sxtx.user.ConfigData
import com.sxtx.user.R
import com.sxtx.user.util.DecryptFile
import com.sxtx.user.util.GlideUtils
import com.sxtx.user.util.ImageUrlRequestUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.MalformedURLException
import java.util.concurrent.TimeUnit

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class HomeGcContentAdapter : BaseQuickAdapter<PublicData.ClassifyVideoData, BaseViewHolder>(R.layout.item_gc_content) {
    //外部控制是否显示svga
    var isShowSvga = true
    var mLruCache: LruCache<String, SVGAVideoEntity>? = null
    var mTotalSize = Runtime.getRuntime().totalMemory().toInt()
    override fun convert(helper: BaseViewHolder?, item: PublicData.ClassifyVideoData?) {
        item?.run {
            GlideUtils.GlideUtil(pictureAddress, helper!!.getView<ImageView>(R.id.img_pic))
            helper.getView<ImageView>(R.id.img_collection)!!.isSelected = isCollect == 1
            helper.getView<ImageView>(R.id.img_collection).setTag(R.id.tv_new_price, isCollect == 1)

            helper.setText(R.id.tv_name, videoName)
            helper.setText(R.id.tv_length, videoTime)
            if (TextUtils.isEmpty(playNum)) helper.setText(R.id.tv_watch, "0") else helper.setText(R.id.tv_watch, playNum)
            if (TextUtils.isEmpty(giveMark)) helper.setText(R.id.tv_star, "0") else helper.setText(R.id.tv_star, giveMark)
            helper.setGone(R.id.tv_tag, isFreeOfCharge == 1)
            helper.addOnClickListener(R.id.img_collection)
            if (item.openSvga == 1 && isShowSvga) {
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
    }
}