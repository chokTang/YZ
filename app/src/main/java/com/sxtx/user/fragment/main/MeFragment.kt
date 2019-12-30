package com.sxtx.user.fragment.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.GridLayout
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.like.base.base.BaseActivity
import com.like.base.base.BaseFragment
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.activity.VideoPlayActivity
import com.sxtx.user.adapter.MeToolAdapter
import com.sxtx.user.adapter.MeVideoAdapter
import com.sxtx.user.fragment.account.ForgetPasswordFragment
import com.sxtx.user.fragment.account.LoginFragment
import com.sxtx.user.fragment.common.WebViewFragment
import com.sxtx.user.fragment.deposit.DepositType1Fragment
import com.sxtx.user.fragment.me.*
import com.sxtx.user.fragment.update.CertificateDialog
import com.sxtx.user.fragment.update.HomeHintDialog
import com.sxtx.user.model.bean.MeToolBean
import com.sxtx.user.mvp.presenter.main.MePresenter
import com.sxtx.user.mvp.view.main.IMeView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.EndlessRecyclerOnScrollListener
import com.sxtx.user.util.GlideImageLoader
import com.sxtx.user.util.UrlCheckUtils
import com.youth.banner.BannerConfig
import event.HeartServiceEvents
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 創建日期：2019/9/23 on 20:40
 * 介紹:
 */
@Suppress("DEPRECATION")
class MeFragment : BaseFragment<MePresenter>(), View.OnClickListener, IMeView {
    override fun getPayData(response: Api.GetPayDataResponse?) {
        when (response?.topYpType) {
            //1-选普通 2-卡密
            1 -> (parentFragment as BaseFragment<*>).start(DepositType1Fragment.newIncetance(1))
            2 -> (parentFragment as BaseFragment<*>).start(DepositType1Fragment.newIncetance(2))
        }
    }

    override fun getResId(): Any {
        return R.layout.fragment_me
    }

    var isBind = false
    var isBindInviteCode = false
    var chatUrl: String = ""
    var qecode: String = ""
    var homeUrl: String = ""
    var mobile = ""
    var welfare = ""
    var adapter: MeToolAdapter? = null
    var collectAdapter: MeVideoAdapter? = null
    var historyAdapter: MeVideoAdapter? = null
    var leftIconList = arrayOf(R.mipmap.me_icon_account, R.mipmap.me_zhaohui, R.mipmap.me_icon_binding, R.mipmap.me_icon_msg,
            R.mipmap.me_icon_service, R.mipmap.me_icon_set)

    var leftTextList = arrayOf("賬號綁定", "賬號找回", "綁定邀請碼", "我的消息",
            "聯繫客服", "設置")

    val list: MutableList<MeToolBean> = arrayListOf()
    val collectList: MutableList<PublicData.ClassifyVideoData> = arrayListOf()
    val historyList: MutableList<PublicData.ClassifyVideoData> = arrayListOf()

    var bannerList: MutableList<PublicData.AdvertisingData> = arrayListOf()
    var isShowCertificate = true

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        ImmersionBar.setTitleBar(getActivity(), status_bar_view_group)
        val gridLayoutManager = GridLayoutManager(activity, 4)
        gridLayoutManager.orientation = GridLayout.VERTICAL
        rv_me_tool.layoutManager = gridLayoutManager
        adapter = MeToolAdapter()
        rv_me_tool.adapter = adapter
        banner.isFocusable = false
        rv_me_tool.isFocusable = false
        collectAdapter = MeVideoAdapter()
        historyAdapter = MeVideoAdapter()
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val manager1 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        rv_my_collection.layoutManager = manager
        rv_my_collection.adapter = collectAdapter
        rv_my_collection.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun toNext() {
                (parentFragment as BaseFragment<*>).start(CollectionFragment())
            }
        })

        rv_watch_history.layoutManager = manager1
        rv_watch_history.adapter = historyAdapter
        rv_watch_history.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun toNext() {
                (parentFragment as BaseFragment<*>).start(RecordFragment())
            }
        })

        setListener()
        EventBus.getDefault().register(this)
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        mPresenter.getUserInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        for (i in 0 until 6) {
            val bean = MeToolBean()
            bean.text = leftTextList[i]
            bean.topIcon = leftIconList[i]
            list.add(bean)
        }
        adapter?.setNewData(list)

        adapter?.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> {//綁定賬號
                    (parentFragment as BaseFragment<*>).start(BingdingPhoneFragment.newInstance(isBind, mobile,qecode,homeUrl))
                }
                1 -> {//找回賬號
                    (parentFragment as BaseFragment<*>).start(ForgetPasswordFragment.newInstance(mobile))
                }
                2 -> {//綁定邀請碼
                    if (isBindInviteCode)
                        (parentFragment as BaseFragment<*>).start(MessageFragment())
                    else
                        (parentFragment as BaseFragment<*>).start(BindingFragment())
                }
                3 -> {//消息
                    if (isBindInviteCode)
                        (parentFragment as BaseFragment<*>).start(WebViewFragment.newIntance("客服", chatUrl))
                    else
                        (parentFragment as BaseFragment<*>).start(MessageFragment())

                }
                4 -> {//联系客服
                    if (isBindInviteCode)
                        (parentFragment as BaseFragment<*>).start(SetFragment())
                    else
                        (parentFragment as BaseFragment<*>).start(WebViewFragment.newIntance("客服", chatUrl))
                }
                5 -> {//設置
                    if (!isBindInviteCode)
                        (parentFragment as BaseFragment<*>).start(SetFragment())
                }

                else -> {

                }
            }

        }

    }

    val handler = object : Handler() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefresh(message: HeartServiceEvents) {
        if (message.eventId == HeartServiceEvents.TO_REFRESH) {
            mPresenter.getUserInfo()
        } else if (message.eventId == HeartServiceEvents.TO_REFRESH_DELI) {
            handler.postDelayed(Runnable { mPresenter.getUserInfo() }, 2000)
        }
    }

    override fun getUserInfoSucc(data: PublicData.ApiUserData?) {
        chatUrl = data?.chatUrl.toString()
        qecode = data?.key.toString()
        homeUrl = data?.homeUrl.toString()
        Glide.with(activity!!).load(data?.headImg).into(img_avatar)
        welfare = data?.welfare.toString()
        tv_user_id.text = "ID：" + data?.apiUserId
        if (data?.parentId?.toInt() == 0) {
            tv_user_parent_id.text = "我的上級：無"
        } else {
            tv_user_parent_id.text = "我的上級：" + data?.parentId
        }
        if (TextUtils.isEmpty(data!!.vipTime)) {
            card_vip.visibility = View.GONE
            img_vip.visibility = View.GONE
            img_novip.visibility = View.VISIBLE
        } else {
            card_vip.visibility = View.VISIBLE
            img_vip.visibility = View.VISIBLE
            img_novip.visibility = View.GONE
            var sp = StringBuilder()
            sp.append(data.vipTime)
            var time = sp.substring(11) + ":00"
            var date = sp.substring(0, 11)
            tv_date.text = date
            tv_time.text = time
        }
        if (data.isShowBindInviteCode != 1) {
            if (data.isNewMessage == 1) {
                list[3].isNewMessage = true
                adapter?.setNewData(list)
            } else {
                list[3].isNewMessage = false
                adapter?.setNewData(list)
            }
        } else {
            if (list.size == 6) {
                list.removeAt(2)
            }
            if (data.isNewMessage == 1) {
                list[2].isNewMessage = true
                adapter?.setNewData(list)
            } else {
                list[2].isNewMessage = false
                adapter?.setNewData(list)
            }
        }
        isBind = data.isBind == 1
        isBindInviteCode = data.isShowBindInviteCode == 1
        mobile = data.mobile

        collectList.clear()
        if (data.collectVideoList == null || data.collectVideoList.size == 0) {
            rv_my_collection.visibility = View.GONE
        } else {
            rv_my_collection.visibility = View.VISIBLE
            collectList.addAll(data.collectVideoList)
            collectAdapter?.setNewData(collectList)
            collectAdapter?.setOnItemClickListener { _, _, position ->
                startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID, collectList[position].videoId))
            }
        }
        historyList.clear()
        if (data.historyVideoList == null || data.historyVideoList.size == 0) {
            rv_watch_history.visibility = View.GONE
        } else {
            rv_watch_history.visibility = View.VISIBLE
            historyList.addAll(data.historyVideoList)
            historyAdapter?.setNewData(historyList)
            historyAdapter?.setOnItemClickListener { _, _, position ->
                startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID, historyList[position].videoId))
            }
        }

        if (!AppPreference.getIntance().saveCertificate){
            CertificateDialog.newIntance()
                    .setContent(qecode)
                    .setGw(homeUrl)
                    .show(childFragmentManager, "show_me_certificate")
        }
    }

    override fun getBannerSucc(list: MutableList<PublicData.AdvertisingData>?) {
        if (list != null) {
            bannerList = list
        }
        if (list?.size == 0)
            fl_item.visibility = View.GONE
        else
            fl_item.visibility = View.VISIBLE
        var bannerlist: MutableList<String> = arrayListOf()
        banner.setImageLoader(GlideImageLoader())
        for (i in 0 until list!!.size) {
            bannerlist.add(list[i].pictureAddress)
        }
        //设置图片集合
        banner.setImages(bannerlist)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        //banner设置方法全部调用完毕时最后调用
        banner.start()

    }

    fun setListener() {
        img_recharge.setOnClickListener(this)
        img_promotion.setOnClickListener(this)
        img_welfare.setOnClickListener(this)
        ll_my_collection.setOnClickListener(this)
        ll_watch_history.setOnClickListener(this)
        banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (ll_desc == null || bannerList.size == 0) return
                if (bannerList.size > position) {
                    if (TextUtils.isEmpty(bannerList[position].describe)) {
                        ll_desc!!.visibility = View.GONE
                    } else {
                        ll_desc!!.visibility = View.VISIBLE
                        tv_desc!!.text = bannerList[position].describe
                    }
                }
            }

        })
        banner.setOnBannerListener { position ->

            if (bannerList[position].goWhere == 1) {
                if (UrlCheckUtils.checkUrlIsEffective(bannerList[position].externalAddress)) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(bannerList[position].externalAddress))))
                    mPresenter.clickAd(bannerList[position].id)
                } else
                    showToast("無效鏈接")
            } else {
                try {
                    val result = Class.forName(bannerList[position].androidAdress) as Class<*>
                    val ob = result.newInstance()
                    if (ob is BaseFragment<*>) {
                        val baseFragment = ob
                        (parentFragment as BaseFragment<*>).start(baseFragment)
                    } else if (ob is BaseActivity<*>) {
                        val intent = Intent(activity, result)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY <= oldScrollY) {//下拉
                if (scrollY <= tv_title.height) {
                    tv_title.visibility = View.GONE
                    status_bar_view_group.background = _mActivity.resources.getDrawable(R.drawable.bg_gradient)
                }
            } else {//上啦
                tv_title.visibility = View.VISIBLE
                if (scrollY - tv_title.height<=10) {
                    val a2 = ObjectAnimator.ofFloat(tv_title, "translationY", 0f, tv_title.height.toFloat())
                    val animSet = AnimatorSet()
                    animSet.duration = 300
                    animSet.play(a2) //先后执行
                    animSet.start()
                }
                status_bar_view_group.background = _mActivity.resources.getDrawable(R.color.white)
            }
        })
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_recharge -> {
                mPresenter.getPayDataResponse()
            }
            R.id.img_promotion -> {
                (parentFragment as BaseFragment<*>).start(TaskFragment.newInstance(true))
            }
            R.id.img_welfare -> {
                if (UrlCheckUtils.checkUrlIsEffective(welfare)) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(welfare))))
                } else
                    showToast("無效鏈接")
            }
            R.id.ll_my_collection -> {
                //收藏
                (parentFragment as BaseFragment<*>).start(CollectionFragment())
            }
            R.id.ll_watch_history -> {
                //觀看歷史
                (parentFragment as BaseFragment<*>).start(RecordFragment())
            }
        }
    }
}
