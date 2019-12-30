package com.sxtx.user.util

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sxtx.user.R

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class GlideOptionUtil {

    companion object{
        fun getOption():RequestOptions{
            val options = RequestOptions()
            options.placeholder(R.mipmap.default_pic)
            options.error(R.mipmap.default_pic)
            options.diskCacheStrategy(DiskCacheStrategy.ALL)
            options.skipMemoryCache(false)
            options.dontAnimate()
            return options
        }
        fun getLiveOption():RequestOptions{
            val options = RequestOptions()
            options.placeholder(R.mipmap.default_pic)
            options.error(R.mipmap.default_pic)
            options.diskCacheStrategy(DiskCacheStrategy.ALL)
            options.skipMemoryCache(false)
            options.dontAnimate()
            return options
        }
        fun getAdOption():RequestOptions{
            val options = RequestOptions()
            options.diskCacheStrategy(DiskCacheStrategy.ALL)
            options.skipMemoryCache(false)
            options.dontAnimate()
            return options
        }
    }
}