package com.like.base.share.qq;

import com.like.base.share.LQQShare;
import com.like.base.share.inter.IShareResult;

/**
 * 分享纯图片实体
 * Created by longshao on 2017/9/8.
 */

public class QImageBuilder {
    private String mQQShareImageUrl;
    private String mQQShareAPPName;//分享的APPname
    private int mQQExtInt;//分享的额外2中选项分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：LQQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。LQQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
    private IShareResult mShareResult;

    public QImageBuilder() {
    }

    /**
     * 设置分享的图片的URL
     *
     * @param mQQShareImageUrl
     * @return
     */
    public final QImageBuilder withImageUrl(String mQQShareImageUrl) {
        this.mQQShareImageUrl = mQQShareImageUrl;
        return this;
    }

    /**
     * 设置APP的名称
     *
     * @param mQQShareAPPName
     * @return
     */
    public final QImageBuilder withAPPName(String mQQShareAPPName) {
        this.mQQShareAPPName = mQQShareAPPName;
        return this;
    }

    /**
     * 设置分享的时候是否额外分享到QQ空间
     * 默认值@-LQQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，@-LQQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE
     *
     * @param mQQExtInt
     * @return
     */
    public final QImageBuilder withQQExtInt(int mQQExtInt) {
        this.mQQExtInt = mQQExtInt;
        return this;
    }

    public final QImageBuilder setShareListener(IShareResult result) {
        this.mShareResult = result;
        return this;
    }

    public final void build() {
        new LQQShare("",
                "",
                "",
                mQQShareImageUrl,
                mQQShareAPPName,
                mQQExtInt,
                mShareResult,
                null).shareImage();
    }
}
