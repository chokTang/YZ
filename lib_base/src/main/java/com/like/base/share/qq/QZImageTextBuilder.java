package com.like.base.share.qq;

import com.like.base.share.LQQShare;
import com.like.base.share.inter.IShareResult;

import java.util.ArrayList;

/**
 * Created by longshao on 2017/9/11.
 */

public class QZImageTextBuilder {

    private String mQQShareTitle;
    private String mQQShareContent;
    private String mQQShareTargetUrl;
    private ArrayList<String> mQZShareImageUrl;

    private IShareResult mShareResult;

    public QZImageTextBuilder() {
    }

    /**
     * 设置QQ分享标题（必填）
     *
     * @param mQQShareTitle
     * @return
     */
    public final QZImageTextBuilder withTitle(String mQQShareTitle) {
        this.mQQShareTitle = mQQShareTitle;
        return this;
    }

    /**
     * 设置QQ分享内容
     *
     * @param mQQShareContent
     * @return
     */
    public final QZImageTextBuilder withContent(String mQQShareContent) {
        this.mQQShareContent = mQQShareContent;
        return this;
    }

    /**
     * 设置QQ分享内容跳转的链接（必填）
     *
     * @param mQQShareTargetUrl
     * @return
     */
    public final QZImageTextBuilder withTargetUrl(String mQQShareTargetUrl) {
        this.mQQShareTargetUrl = mQQShareTargetUrl;
        return this;
    }

    /**
     * 设置分享的图片的URL
     *
     * @param mQZShareImageUrl
     * @return
     */
    public final QZImageTextBuilder withImageUrl(ArrayList<String> mQZShareImageUrl) {
        this.mQZShareImageUrl = mQZShareImageUrl;
        return this;
    }

    public final QZImageTextBuilder setShareListener(IShareResult result) {
        this.mShareResult = result;
        return this;
    }

    public final void build() {
        new LQQShare(mQQShareTitle,
                mQQShareContent,
                mQQShareTargetUrl,
                "",
                "",
                0,
                mShareResult,
                mQZShareImageUrl).shareQZoneImageText();
    }
}
