package com.sxtx.user.util

import android.widget.ImageView
import com.like.utilslib.other.LogUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.InputStream
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 * 介紹: 线程池用法
 * 作者:CHOK
 */
open class ImageUrlRequestUtil {


    companion object {


        //创建线程的工厂
        var threadFactory: ThreadFactory = object : ThreadFactory {
            var integer = AtomicInteger()

            override fun newThread(r: Runnable): Thread {
                return Thread(r, "RoomDataThread:" + integer.getAndIncrement())
            }
        }
        var singleExecutorService = Executors.newCachedThreadPool(threadFactory)

        //1.初始化OKHttp
        val okHttpClient = OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                .build()


        /**
         * 获取图片 相信信息
         */
        fun getImageUrl(url: String,view:ImageView, block: (inputStream: InputStream,view:ImageView) -> Unit) {
            //2.构建request
            val request = Request.Builder()
                    .url(url)
                    .build()
            var execute: Response? = null
            //待会儿在这里实现OkHttp请求
            //3.发送请求（这里并没有执行，只是构建了`RealCall`对象）

            try {
                singleExecutorService.execute {
                    try {
                        val call = okHttpClient.newCall(request)
                        //4.接收响应（请求执行，并得到响应结果）
                        execute = call.execute()
                        block(execute!!.body()!!.byteStream(),view)
                    } catch (e: Exception) {
                        LogUtil.loge("圖片URL$e")
                    }
                }
            } catch (e: IllegalArgumentException) {
                LogUtil.loge("圖片URL$e")
            } catch (e: RuntimeException) {
                LogUtil.loge("圖片URL$e")
            }
        }

    }


}