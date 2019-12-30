package com.like.base.share.inter;

/**
 * 分享回调
 * Created by longshao on 2017/9/8.
 */

public interface IShareResult {

    void onShareSuccess();//分享成功

    void onShareError(String message);//分享失败

    void onShareCancle();//取消分享
}
