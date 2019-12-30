package com.sxtx.user.mvp.view.account;


import com.lyh.protocol.data.PublicData;

import java.util.List;

/**
 * Created by Administrator on 2017/12/30.
 */

public interface ILoginView {
    void sendHeart();
    void getDataSucc(List<PublicData.AdvertisingData> list);
    void loginSucc();

}
