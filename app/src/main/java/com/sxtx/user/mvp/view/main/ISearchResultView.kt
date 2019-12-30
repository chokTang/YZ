package com.sxtx.user.mvp.view.main

import com.lyh.protocol.data.PublicData

interface ISearchResultView {

    fun sureSearchList(videoList: List<PublicData.ClassifyVideoData>);

    fun collection(position:Int,type:Int)


    fun error()



}