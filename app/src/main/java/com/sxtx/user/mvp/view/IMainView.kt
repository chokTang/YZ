package com.sxtx.user.mvp.view

import com.lyh.protocol.login.Api

interface IMainView {
    fun getPayData(respons: Api.GetPayDataResponse)

    fun getAppVersionSucceed(androinVersion : String,androinAddress : String,content:String,title :String)

    fun showDownloadProgressDialog();

    fun updateDownloadProgress(progress : Int, total: Int);

    fun showDownloadErrorDialog();

}