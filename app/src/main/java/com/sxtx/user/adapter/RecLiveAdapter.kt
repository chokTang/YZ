package com.sxtx.user.adapter

import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.like.utilslib.app.CommonUtil
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.util.GlideImageLoader
import com.sxtx.user.util.GlideOptionUtil
import com.sxtx.user.util.GlideUtils
import com.youth.banner.Banner
import com.youth.banner.BannerConfig

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class RecLiveAdapter : BaseQuickAdapter<PublicData.AnchorData, BaseViewHolder>(R.layout.item_rec_live) {

    companion object {
        const val TYPE_1 = 1  //主播
        const val TYPE_2 = 2  //廣告
    }

    init {
        multiTypeDelegate = object : MultiTypeDelegate<PublicData.AnchorData>() {
            override fun getItemType(t: PublicData.AnchorData?): Int {
                var type = TYPE_1
                type = if (CommonUtil.isNull(t?.liveBannerList) || t?.liveBannerList!!.size == 0) {
                    TYPE_1
                } else {
                    TYPE_2
                }
                return type
            }
        }

        multiTypeDelegate.registerItemType(TYPE_1, R.layout.item_rec_live)
                .registerItemType(TYPE_2, R.layout.item_live_banner_1)
    }


    override fun convert(helper: BaseViewHolder?, item: PublicData.AnchorData?) {
        when (helper?.itemViewType) {
            TYPE_1 -> {
                item?.run {
                    LogUtil.loge(bannerImg)
                    Glide.with(mContext).load(bannerImg).thumbnail(GlideUtils.thumbnails).apply(GlideOptionUtil.getLiveOption()).into(helper!!.getView<ImageView>(R.id.img_pic))
                    helper.setText(R.id.tv_name, name)
                    helper.setText(R.id.tv_people, watchTv.toString())
                    helper.setGone(R.id.tv_tag, isFree == 1)
                }
            }
            TYPE_2 -> {
                val banner = helper.getView<Banner>(R.id.banner)
                val bannerlist: MutableList<String> = arrayListOf()
                banner.setImageLoader(GlideImageLoader())
                if (!(CommonUtil.isNull(item!!.liveBannerList) || item.liveBannerList!!.size == 0)) {
                    helper.setText(R.id.tv_desc, item.liveBannerList[0].describe)
                }
                for (i in 0 until item.liveBannerList.size) {
                    bannerlist.add(item.liveBannerList[i].pictureAddress)
                }
                //设置图片集合
                banner.setImages(bannerlist)
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER)
                //banner设置方法全部调用完毕时最后调用
                banner.start()
                banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    }

                    override fun onPageSelected(position: Int) {
                        if (item.liveBannerList.size == 0)
                            return
                        if (item.liveBannerList.size > position){
                            if (TextUtils.isEmpty(item.liveBannerList[position].describe)) {
                                helper.setGone(R.id.ll_desc, false)
                            } else {
                                helper.setGone(R.id.ll_desc, true)
                                if (position>0){
                                    helper.setText(R.id.tv_desc, item.liveBannerList[position].describe)
                                }
                            }
                        }
                    }

                })
                banner.setOnBannerListener { position ->
                    bannerClick?.click(position, item.liveBannerList[position])
                }
            }
        }

    }

    interface BannerCLick {
        fun click(position: Int, bean: PublicData.AdvertisingData)
    }

    var bannerClick: BannerCLick? = null

    operator fun invoke(bannerClick: BannerCLick?) {
        this.bannerClick = bannerClick
    }
}