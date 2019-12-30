package com.sxtx.user.util

import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.like.utilslib.UtilApp


/**
 *
 * 介紹:
 * 作者:CHOK
 */
class GlideUtils {


    companion object {
        val thumbnails = 0.1f
//        var mMemoryCache: LruCache<String, Bitmap>? = null
//        fun initLruCache() {
//            try {
//                val maxMemory = Runtime.getRuntime().maxMemory()d
//                val cacheSize = maxMemory / 4
//                // 设置缓存大小为程序最大可用内存的1/4
//                mMemoryCache = LruCache(cacheSize.toInt())
//            } catch (e: Exception) {
//                LogUtil.loge(e.toString())
//            }
//        }

        fun GlideUtil(pictureAddress: String, view: ImageView) {
            if (!TextUtils.isEmpty(pictureAddress)) {
                Glide.with(UtilApp.getIntance().applicationContext).load(pictureAddress).apply(GlideOptionUtil.getOption()).into(view)
            }

//            ImageUrlRequestUtil.getImageUrl("",view){ inputStream, view->
//
//            }

//            }else{
//                Glide.with(UtilApp.getIntance().applicationContext).load(R.mipmap.default_pic).thumbnail(thumbnails).apply(GlideOptionUtil.getOption()).into(view)
//            }
        }


    }


}