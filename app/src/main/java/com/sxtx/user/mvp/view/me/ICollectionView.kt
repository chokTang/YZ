package com.sxtx.user.mvp.view.me

import com.lyh.protocol.data.PublicData

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface ICollectionView {
    abstract fun getCheckCollectRecrdReqeustData(videoList: List<PublicData.ClassifyVideoData>)


    fun collection(position:Int,type:Int)


    fun cancleCollection(position:Int)



}