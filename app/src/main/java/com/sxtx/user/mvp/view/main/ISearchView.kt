package com.sxtx.user.mvp.view.main

import com.google.protobuf.ProtocolStringList

interface ISearchView {

     fun clickSearchReqeustSucceed(apiUserKeyWord: ProtocolStringList,sysUserKeyWord: ProtocolStringList);


     fun clickRemoveSucceed();


}