package com.sxtx.user.mvp.view.main;

import com.lyh.protocol.data.PublicData;
import com.lyh.protocol.login.Api;

import java.util.List;

/**
 * 創建日期：2019/9/23 on 20:37
 * 介紹:
 * 作者:CHOK
 */
public interface IMeView {
    void getUserInfoSucc(PublicData.ApiUserData data);
    void getBannerSucc(List<PublicData.AdvertisingData> list);
    void getPayData(Api.GetPayDataResponse response);
}
