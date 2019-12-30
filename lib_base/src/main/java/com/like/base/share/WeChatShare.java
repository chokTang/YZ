package com.like.base.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.like.base.BuildConfig;
import com.like.base.share.inter.IRequestListener;
import com.like.base.share.inter.IShareResult;
import com.like.base.wechat.LongWeChat;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by longshao on 2017/9/8.
 */

public class WeChatShare {

    private static final int THUMB_SIZE = 200;
    private IShareResult shareResult;

    private WeChatShare() {
    }

    private static class Holder {
        private static WeChatShare INTANCE = new WeChatShare();
    }

    public static final WeChatShare getIntance() {
        return Holder.INTANCE;
    }

    public WeChatShare setShareResult(IShareResult shareResult) {
        this.shareResult = shareResult;
        return this;
    }

    /**
     * 分享到朋友聊天页面
     *
     * @param content 内容
     * @param descri  描述
     */
    public final void shareToChatText(String content, String descri) {
        shareToText(content, descri, true);
    }

    /**
     * 分享到朋友圈页面
     *
     * @param content 内容
     * @param descri  描述
     */
    public final void shareToFriendText(String content, String descri) {
        shareToText(content, descri, false);
    }

    /**
     * 微信纯图片分享到聊天页面
     *
     * @param bitmap
     */
    public final void shareToChatImage(Bitmap bitmap) {
        shareToImage(bitmap, true);
    }

    /**
     * 微信纯图片分享到朋友圈
     *
     * @param bitmap
     */
    public final void shareToFriendImage(Bitmap bitmap) {
        shareToImage(bitmap, false);
    }

    /**
     * 分享webUrl到微信好友
     *
     * @param title
     * @param description
     * @param bitmap
     * @param tagUrl
     */
    public final void shareToChatWeb(String title, String description, Object bitmap, String tagUrl) {
        shareToWeb(title, description, bitmap, tagUrl, SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 分享webUrl到微信朋友圈
     *
     * @param title
     * @param description
     * @param bitmap
     * @param tagUrl
     */
    public final void shareToFriendWeb(String title, String description, Object bitmap, String tagUrl) {
        shareToWeb(title, description, bitmap, tagUrl, SendMessageToWX.Req.WXSceneTimeline);
    }

    /**
     * 收藏weburl到微信
     *
     * @param title
     * @param description
     * @param bitmap
     * @param tagUrl
     */
    public final void shareToCollectionWeb(String title, String description, Object bitmap, String tagUrl) {
        shareToWeb(title, description, bitmap, tagUrl, SendMessageToWX.Req.WXSceneFavorite);
    }

    /**
     * 发送web文本
     *
     * @param title
     * @param description
     * @param bitmap
     * @param tagUrl
     * @param sceneType
     */
    private final void shareToWeb(String title, String description, Object bitmap, String tagUrl, final int sceneType) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = tagUrl;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        if (bitmap instanceof Bitmap) {
            Bitmap thumbBmp = Bitmap.createScaledBitmap((Bitmap) bitmap, THUMB_SIZE, THUMB_SIZE, true);
            msg.thumbData = bitmapToByteArray(thumbBmp, true);

            //构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = msg;
            req.scene = sceneType;
            req.userOpenId = BuildConfig.WX_APPID;

            //调用api接口，发送数据到微信
            LongWeChat.getIntance().getWXAPI().sendReq(req);
        } else if (bitmap instanceof String) {
            urlToBitmap(bitmap.toString(), new IRequestListener() {
                @Override
                public void onRequestSucess(Bitmap bitmap) {
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
                    msg.thumbData = bitmapToByteArray(thumbBmp, true);

                    //构造一个Req
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = sceneType;
                    req.userOpenId = BuildConfig.WX_APPID;

                    //调用api接口，发送数据到微信
                    LongWeChat.getIntance().getWXAPI().sendReq(req);
                }

                @Override
                public void onRequestFaile() {
                    if (shareResult != null) {
                        shareResult.onShareError("图片下载错误");
                    }
                }
            });
            return;
        } else {
            if (shareResult != null) {
                shareResult.onShareError("图片路径错误");
            }
            return;
        }
    }

    /**
     * 分享纯文本消息
     *
     * @param content
     * @param descri
     * @param isChat  是否分享到聊天页面
     */
    private final void shareToText(String content, String descri, boolean isChat) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = content;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = descri;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = isChat ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        LongWeChat.getIntance().getWXAPI().sendReq(req);
    }

    /**
     * 分享纯图片
     *
     * @param bitmap
     * @param isChat 是否分享到聊天页面
     */
    private final void shareToImage(Bitmap bitmap, boolean isChat) {
        if (bitmap == null)
            return;
        WXImageObject imgObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObject;

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = bitmapToByteArray(thumbBitmap, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = isChat ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        LongWeChat.getIntance().getWXAPI().sendReq(req);
    }

    /**
     * 初始化类型
     *
     * @param type
     * @return
     */
    private final String buildTransaction(String type) {
        return type + String.valueOf(System.currentTimeMillis());
    }

    public IShareResult getShareResult() {
        return shareResult;
    }

    /**
     * 微信调用
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    private final byte[] bitmapToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void urlToBitmap(final String url, final IRequestListener listener) {
        Observable downObser = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter oe) throws Exception {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    oe.onError(e);
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    oe.onNext(bitmap);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    oe.onError(e);
                }
                oe.onComplete();
            }
        });

        downObser.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableObserver<Bitmap>() {
            @Override
            public void onNext(Bitmap value) {
                listener.onRequestSucess(value);
            }

            @Override
            public void onError(Throwable e) {
                listener.onRequestFaile();
            }

            @Override
            public void onComplete() {

            }
        });//把这段代码连起来写就成了RxJava引以为傲的链式操作
    }
}
