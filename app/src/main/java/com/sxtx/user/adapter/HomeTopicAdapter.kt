package com.sxtx.user.adapter

import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.like.utilslib.app.CommonUtil
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.util.GlideImageLoader
import com.sxtx.user.util.GlideUtils
import com.youth.banner.Banner
import com.youth.banner.BannerConfig

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class HomeTopicAdapter : BaseQuickAdapter<PublicData.SpecialData, BaseViewHolder>(R.layout.item_home_new){
    var type = 1 //1顯示下面bottom  2 不顯示bottom

    override fun convert(helper: BaseViewHolder?, item: PublicData.SpecialData?) {
        item?.run {
            GlideUtils.GlideUtil(pictureAddress, helper!!.getView<ImageView>(R.id.img_pic))
            helper.setText(R.id.tv_name, name)
            helper.setGone(R.id.img_collection, false)
            helper.setGone(R.id.tv_time, false)
            helper.setGone(R.id.tv_star, false)
            helper.setGone(R.id.ll_eye, false)
            helper.addOnClickListener(R.id.img_collection)

        }

        if (CommonUtil.isNull(item?.specialBannerList) ||item?.specialBannerList!!.size == 0) {
            helper!!.setGone(R.id.fl_item, false)
        } else {
            helper!!.setGone(R.id.fl_item, true)
            helper.setText(R.id.tv_desc, item.specialBannerList[0].describe)
            val banner = helper.getView<Banner>(R.id.banner)
            val bannerlist: MutableList<String> = arrayListOf()
            banner.setImageLoader(GlideImageLoader())
            for (i in 0 until item.specialBannerList.size) {
                bannerlist.add(item.specialBannerList[i].pictureAddress)
            }
            //设置图片集合
            banner.setImages(bannerlist)
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            banner.start()
            banner.setOnPageChangeListener(object :ViewPager.OnPageChangeListener{
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }


                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageSelected(position: Int) {
                    if (item.specialBannerList.size == 0)
                        return
                    if (item.specialBannerList.size > position){
                        if (TextUtils.isEmpty(item.specialBannerList[position].describe)) {
                            helper.setGone(R.id.ll_desc,false)
                        } else {
                            helper.setGone(R.id.ll_desc,true)
                            if (position>0){
                                helper.setText(R.id.tv_desc, item.specialBannerList[position].describe)
                            }
                        }
                    }

                }

            })
            banner.setOnBannerListener { position ->
                bannerClick?.click(position, item.specialBannerList[position]) }
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