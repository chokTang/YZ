package com.like.base.share.qq;

import com.like.base.share.LQQShare;
import com.like.base.share.inter.IShareResult;

/**
 * 分享图片和文本消息实体
 * Created by longshao on 2017/9/8.
 */

public class QImageTextBuilder {

    private String mQQShareTitle;
    private String mQQShareContent;
    private String mQQShareTargetUrl;
    private String mQQShareImageUrl;
    private String mQQShareAPPName;//分享的APPname
    private int mQQExtInt;//分享的额外2中选项分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：LQQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。LQQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮

    private IShareResult mShareResult;

    public QImageTextBuilder() {
    }

    /**
     * 设置QQ分享标题（必填）
     *
     * @param mQQShareTitle
     * @return
     */
    public final QImageTextBuilder withTitle(String mQQShareTitle) {
        this.mQQShareTitle = mQQShareTitle;
        return this;
    }

    /**
     * 设置QQ分享内容
     *
     * @param mQQShareContent
     * @return
     */
    public final QImageTextBuilder withContent(String mQQShareContent) {
        this.mQQShareContent = mQQShareContent;
        return this;
    }

    /**
     * 设置QQ分享内容跳转的链接（必填）
     *
     * @param mQQShareTargetUrl
     * @return
     */
    public final QImageTextBuilder withTargetUrl(String mQQShareTargetUrl) {
        this.mQQShareTargetUrl = mQQShareTargetUrl;
        return this;
    }

    /**
     * 设置分享的图片的URL
     *
     * @param mQQShareImageUrl
     * @return
     */
    public final QImageTextBuilder withImageUrl(String mQQShareImageUrl) {
        this.mQQShareImageUrl = mQQShareImageUrl;
        return this;
    }

    /**
     * 设置APP的名称
     *
     * @param mQQShareAPPName
     * @return
     */
    public final QImageTextBuilder withAPPName(String mQQShareAPPName) {
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
    public final QImageTextBuilder withQQExtInt(int mQQExtInt) {
        this.mQQExtInt = mQQExtInt;
        return this;
    }

    public final QImageTextBuilder setShareListener(IShareResult result) {
        this.mShareResult = result;
        return this;
    }

    public final void build() {
        new LQQShare(mQQShareTitle,
                mQQShareContent,
                mQQShareTargetUrl,
                mQQShareImageUrl,
                mQQShareAPPName,
                mQQExtInt,
                mShareResult,
                null).shareImageText();
    }
}
