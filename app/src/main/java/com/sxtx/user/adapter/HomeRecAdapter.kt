package com.sxtx.user.adapter

import android.graphics.drawable.Drawable
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.like.base.adapter.rvhelper.DividerGridItemDecoration
import com.like.utilslib.app.CommonUtil
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.util.GlideImageLoader
import com.sxtx.user.util.GlideUtils
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.item_home_rec_2.view.*


/**
 *
 * 介紹:
 * 作者:CHOK
 */
class HomeRecAdapter : BaseQuickAdapter<PublicData.RecommendData, HomeRecAdapter.RecViewHolder>(R.layout.item_home_rec_2){

    var list:MutableList<PublicData.ClassifyVideoData> = arrayListOf()

    var position = -1

    override fun convert(helper: RecViewHolder?, item: PublicData.RecommendData?) {
        val gridLayoutManager = GridLayoutManager(mContext, 2)
        helper?.getView<RecyclerView>(R.id.rv_rec_content)!!.layoutManager = gridLayoutManager
        helper.getView<RecyclerView>(R.id.rv_rec_content)!!.adapter = helper.adapter
        helper.getView<RecyclerView>(R.id.rv_rec_content)!!.isFocusable = false
        helper.getView<Banner>(R.id.banner)!!.isFocusable = false
        helper.addOnClickListener(R.id.tv_more)
        helper.addOnClickListener(R.id.img_pic)
        val banner = helper.getView<Banner>(R.id.banner)
        banner.setOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (item!!.recommendBannerList.size == 0)
                    return
                if (item!!.recommendBannerList.size > position){
                    if (TextUtils.isEmpty(item.recommendBannerList[position].describe)) {
                        helper.setGone(R.id.ll_desc,false)
                    } else {
                        helper.setGone(R.id.ll_desc,true)
                        if (position>0){
                            helper.setText(R.id.tv_desc, item.recommendBannerList[position].describe)
                        }
                    }
                }
            }

        })
        item?.run {
            helper.setText(R.id.tv_title, recommendName)
            var drawright: Drawable? = null
            if (isMore==1){
                drawright = mContext.resources.getDrawable(R.mipmap.icon_enter_black)
                helper.setText(R.id.tv_more, "更多")
            }else{
                drawright = mContext.resources.getDrawable(R.mipmap.icon_refresh)
                helper.setText(R.id.tv_more, "換一批")
            }
            drawright!!.setBounds(0, 0, drawright.minimumWidth, drawright.minimumHeight)
            helper.getView<TextView>(R.id.tv_more)!!.setCompoundDrawables(null, null, drawright, null)

            if (helper.adapterPosition == 0) {
                helper.setGone(R.id.ll_first, false)
                if (list.size>0){
                    helper.adapter!!.setNewData(list)
                }else{
                    helper.adapter!!.setNewData(videoDataList)
                }
            } else {
                helper.setGone(R.id.ll_first, true)
                if (!CommonUtil.isNull(videoDataList)&&videoDataList.size>0){
                    helper.setText(R.id.tv_name, videoDataList[0].videoName)
                    helper.setText(R.id.tv_length, videoDataList[0].videoTime)
                    helper.setGone(R.id.tv_tag, videoDataList[0].isFreeOfCharge == 1)

                    GlideUtils.GlideUtil(videoDataList[0].pictureAddress, helper.getView<ImageView>(R.id.img_pic))
                    if (TextUtils.isEmpty(videoDataList[0].playNum))helper.setText(R.id.tv_watch, "0") else helper.setText(R.id.tv_watch, videoDataList[0].playNum)
                    if (TextUtils.isEmpty(videoDataList[0].giveMark))helper.setText(R.id.tv_star, "0")else helper.setText(R.id.tv_star, videoDataList[0].giveMark)
                    if (list.size>0){
                        helper.adapter!!.setNewData(list)
                    }else{
                        helper.adapter!!.setNewData(videoDataList.subList(1, videoDataList.size))
                    }
                    helper.getView<ImageView>(R.id.img_collection)!!.isSelected = videoDataList[0].isCollect==1
                    helper.getView<ImageView>(R.id.img_collection).setTag(R.id.tv_new_price,videoDataList[0].isCollect==1)

                    helper.addOnClickListener(R.id.img_collection)
                }

            }
            if (position==-1){//说明不是在点击第一个view的收藏 菜刷新当钱的banner数据,点击第一个view收藏时候不刷新
                if (CommonUtil.isNull(recommendBannerList) || recommendBannerList.size == 0) {
                    helper.setGone(R.id.fl_item, false)
                } else {
                    helper.setGone(R.id.fl_item, true)
                    val bannerlist: MutableList<String> = arrayListOf()
                    if (!(CommonUtil.isNull(item.recommendBannerList) || item.recommendBannerList!!.size == 0)) {
                        helper.setText(R.id.tv_desc, item.recommendBannerList[0].describe)
                    }
                    banner.setImageLoader(GlideImageLoader())
                    for (i in 0 until recommendBannerList.size) {
                        bannerlist.add(recommendBannerList[i].pictureAddress)
                    }
                    //设置图片集合
                    banner.setImages(bannerlist)
                    //设置指示器位置（当banner模式中有指示器时）
                    banner.setIndicatorGravity(BannerConfig.CENTER)
                    //banner设置方法全部调用完毕时最后调用
                    banner.start()
                    banner.setOnBannerListener { position ->
                        bannerClick?.click(position, item.recommendBannerList[position]) }
                }
            }

            helper.adapter?.setOnItemChildClickListener { _, view, position ->
                clickListener?.click(helper.adapterPosition, position,view,false)
            }
            helper.adapter?.setOnItemClickListener { _, view, position ->
                if (helper.adapterPosition == 0){
                    clickListener?.click(helper.adapterPosition, position,view,true)
                }else{
                    clickListener?.click(helper.adapterPosition, position+1,view,true)
                }
            }

        }
//        helper.setIsRecyclable(false)
    }


    class RecViewHolder(view: View) : BaseViewHolder(view) {
        var adapter: HomeGcContentAdapter? = null
        init {
            adapter = HomeGcContentAdapter()
            view.rv_rec_content.addItemDecoration(DividerGridItemDecoration(R.drawable.listdivider_white_16))
        }
    }



    interface BannerCLick {
        fun click(position: Int, bean: PublicData.AdvertisingData)
    }

    var bannerClick: BannerCLick? = null

    operator fun invoke(bannerClick: BannerCLick?) {
        this.bannerClick = bannerClick
    }

    interface ClickListener {
        fun click(firstPosion: Int, secondPosion: Int,view: View,isItemCLick: Boolean)
    }

    var clickListener: ClickListener? = null

    operator fun invoke(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }

}

