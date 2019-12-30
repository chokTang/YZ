package com.sxtx.user.fragment.deposit

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.like.base.adapter.rvhelper.DividerGridItemDecoration
import com.like.base.adapter.rvhelper.DividerItemDecoration
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.like.utilslib.app.CommonUtil
import com.like.utilslib.json.GSONUtil
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.adapter.*
import com.sxtx.user.fragment.common.WebViewFragment
import com.sxtx.user.fragment.me.BingdingPhoneFragment
import com.sxtx.user.fragment.update.HomeHintDialog
import com.sxtx.user.model.bean.PayBean
import com.sxtx.user.model.bean.PayFinishBean
import com.sxtx.user.model.bean.VipButtonBean
import com.sxtx.user.mvp.presenter.deposit.DeoisutType1Presonter
import com.sxtx.user.mvp.view.IDeoisutType1View
import kotlinx.android.synthetic.main.fragment_deposit_1.*

/**
 *
 * 介紹:  第一種充值方式
 * 作者:CHOK
 */
class DepositType1Fragment : BaseStatusToolbarFragment<DeoisutType1Presonter>(), IDeoisutType1View, View.OnClickListener {

    private var runnable: Runnable? = null
    private val handler = object : Handler() {}
    var finishUrl = ""
    var orderNo = ""
    var adapter: DepositTcAdapter? = null
    var vipButtonAdapter: VipButtonAdapter? = null
    var type1Adapter: PayType1Adapter? = null
    var type2Adapter: PayType2Adapter? = null
    var lxrAdapter: LxrAdapter? = null
    var mchatUrl = ""
    var qrcode = ""
    var homeUrl = ""
    var phone = ""
    var isBind = false
    val vipbuttonList: MutableList<VipButtonBean> = arrayListOf()

    var isReQuestPay = false

    var type = 1

    var type1position = 0
    var type2position = 0
    var type3position = 0
    var response1: Api.GetPayDataResponse? = null

    companion object {
        const val KEY_TYPE = "ketdepositTYpe"  //顯示那種充值方式頁面 1 顯示1 直接充值  2顯示2的卡密充值充值頁面
        fun newIncetance(type: Int): DepositType1Fragment {
            val fragment = DepositType1Fragment()
            val bundle = Bundle()
            bundle.putInt(KEY_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun getMainResId(): Int {
        return R.layout.fragment_deposit_1
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        onBack(com.like.base.R.drawable.toolbar_back_white)
        setToolbarTitle("充值中心")
        mToolbar.setBackgroundColor(resources.getColor(R.color.color_1D2028))
        mToolbarTitle.setTextColor(resources.getColor(R.color.color_E3BC7D))
        mToolbarRightTitle.setTextColor(resources.getColor(R.color.color_E0E1E2))
        setShowLineTitle(false)
        setToolbarRightTitle("購買記錄")
        showView(BaseFragment.MAIN_VIEW)
        setListener()

        val bundle = arguments
        type = bundle!!.getInt(KEY_TYPE, 1)
        showType(type)

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_tc.layoutManager = linearLayoutManager
        adapter = DepositTcAdapter()
        rv_tc.adapter = adapter
        rv_tc.addItemDecoration(DividerItemDecoration(DividerItemDecoration.HORIZONTAL_LIST, R.drawable.listdivider_white_16))


        val linearLayoutManager4 = GridLayoutManager(activity, 4)
        rv_vip_button.layoutManager = linearLayoutManager4
        vipButtonAdapter = VipButtonAdapter()
        rv_vip_button.adapter = vipButtonAdapter

//        val linearLayoutManager1 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager1 = GridLayoutManager(activity, 4)
        rv_pay_type_1.layoutManager = linearLayoutManager1
        type1Adapter = PayType1Adapter()
        rv_pay_type_1.adapter = type1Adapter
        rv_pay_type_1.addItemDecoration(DividerGridItemDecoration(R.drawable.listdivider_white_10))


        val linearLayoutManager2 = LinearLayoutManager(activity)
        rv_pay_type_2.layoutManager = linearLayoutManager2
        type2Adapter = PayType2Adapter()
        rv_pay_type_2.adapter = type2Adapter
        rv_pay_type_2.addItemDecoration(DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, R.drawable.listdivider_window_1))


        val linearLayoutManager3 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_lxr.layoutManager = linearLayoutManager3
        lxrAdapter = LxrAdapter()
        rv_lxr.adapter = lxrAdapter
        rv_lxr.addItemDecoration(DividerItemDecoration(DividerItemDecoration.HORIZONTAL_LIST, R.drawable.listdivider_white_16))


    }

    fun setListener() {
        ll_type_1_bottom.setOnClickListener(this)
        tv_sure2.setOnClickListener(this)
        rightTitle.setOnClickListener {
            start(BuyHistoryFragment())
        }
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)



        mPresenter.getUserInfo()

        for (i in 0 until 4) {
            val bean = VipButtonBean()
            vipbuttonList.add(bean)
        }
        vipButtonAdapter?.setNewData(vipbuttonList)
        vipButtonAdapter?.setOnItemClickListener { adapter, view, position ->
            //            showToast("你點擊了$position")
        }


        adapter?.setOnItemClickListener { adapter, _, position ->
            this.adapter!!.position = position
            type3position = position
            showPrice(response1!!)
            adapter.notifyDataSetChanged()
            if (position > 0 && position != adapter.data.size - 1) {
                rv_tc.scrollToPosition(position - 1)
            } else {
                rv_tc.scrollToPosition(position)
            }
        }

        //填充充值方式1數據  直衝
        type1Adapter?.setOnItemClickListener { adapter, _, position ->
            this.type1Adapter!!.position = position
            type1position = position
            type2position = 0
            type1Adapter?.position = position
            type1Adapter?.setNewData(response1!!.normalTop.paySortList)
            //點擊充值方式時候 談出充值通道默認選擇0
            type2Adapter?.position = 0
            type2Adapter?.setNewData(response1!!.normalTop.paySortList[position].dataList)
            if (response1!!.normalTop.paySortList[position].dataList.size > 0) {
                rv_tc.visibility = View.VISIBLE
                ll_type_1_bottom.visibility = View.VISIBLE
//                this.adapter?.position = zcDefaultSelect
                this.adapter?.setNewData(response1!!.normalTop.paySortList[position].dataList[0].payMoneyList)
                type3position = getIndex(response1!!.normalTop.paySortList[position].dataList[0].payMoneyList)
//                rv_tc.scrollToPosition(getIndex(response1!!.normalTop.paySortList[position].dataList[0].payMoneyList))
                if (getIndex(response1!!.normalTop.paySortList[position].dataList[0].payMoneyList) > 0 && getIndex(response1!!.normalTop.paySortList[position].dataList[0].payMoneyList) != response1!!.normalTop.paySortList[position].dataList[0].payMoneyList.size - 1) {
                    rv_tc.scrollToPosition(getIndex(response1!!.normalTop.paySortList[position].dataList[0].payMoneyList) - 1)
                } else {
                    rv_tc.scrollToPosition(getIndex(response1!!.normalTop.paySortList[position].dataList[0].payMoneyList))
                }
                if (response1!!.normalTop.paySortList[position].dataList[0].payMoneyList.size == 0) {
                    rv_tc.visibility = View.GONE
                    ll_type_1_bottom.visibility = View.GONE
                } else {
                    rv_tc.visibility = View.VISIBLE
                    ll_type_1_bottom.visibility = View.VISIBLE
                }
            } else {
                rv_tc.visibility = View.GONE
                ll_type_1_bottom.visibility = View.GONE
                val emptyView = LayoutInflater.from(context).inflate(R.layout.item_empty_deposit, null, false)
                type2Adapter!!.emptyView = emptyView
            }
            showPrice(response1!!)
            adapter.notifyDataSetChanged()
        }

        type2Adapter?.setOnItemClickListener { adapter, _, position ->
            type2Adapter?.position = position
            type2position = position
            type3position = getIndex(response1!!.normalTop.paySortList[type1position].dataList[position].payMoneyList)
//            this.adapter?.position = kmDefaultSelect
            this.adapter?.setNewData(response1!!.normalTop.paySortList[type1position].dataList[position].payMoneyList)
//            rv_tc.scrollToPosition(getIndex(response1!!.normalTop.paySortList[type1position].dataList[position].payMoneyList))
            if (getIndex(response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList) > 0 && getIndex(response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList) != response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList.size - 1) {
                rv_tc.scrollToPosition(getIndex(response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList) - 1)
            } else {
                rv_tc.scrollToPosition(getIndex(response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList))
            }
            if (response1!!.normalTop.paySortList[type1position].dataList[position].payMoneyList.size == 0) {
                rv_tc.visibility = View.GONE
                ll_type_1_bottom.visibility = View.GONE
            } else {
                rv_tc.visibility = View.VISIBLE
                ll_type_1_bottom.visibility = View.VISIBLE

            }
            showPrice(response1!!)
            adapter.notifyDataSetChanged()
        }


        lxrAdapter?.setOnItemClickListener { adapter, view, position ->
            //            showToast("你點擊了$position")
        }
    }


    @SuppressLint("SetTextI18n")
    override fun getPayData(respons: Api.GetPayDataResponse) {
        adapter?.type = respons.topYpType
        if (respons.topYpType == 1) {//1-选普通 2-卡密
            response1 = respons
            tv_user_id.text = "ID:${respons.normalTop.userId}"
            if (respons.normalTop.vipTime == "未開通VIP特權") {
                img_vip.visibility = View.GONE
                tv_vip_time.text = "未開通VIP特權"
            } else {
                img_vip.visibility = View.VISIBLE
                val msp = SpannableString(respons.normalTop.vipTime)
                msp.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_F4C983)), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_vip_time.text = msp
            }


            //刷新充值方式  默認選第0個
            type1Adapter?.position = 0
            type1Adapter?.setNewData(respons.normalTop.paySortList)
            //刷新某種充值方式下面的通道 默認選第0個
            type2Adapter?.position = 0

            if (respons.normalTop.paySortList.size > 0) {
                //刷新頂部套餐數據
                if (respons.normalTop.paySortList[0].dataList.size > 0) {
                    type2Adapter?.setNewData(respons.normalTop.paySortList[0].dataList)
                    rv_tc.visibility = View.VISIBLE
                    ll_type_1_bottom.visibility = View.VISIBLE
                    this.adapter?.setNewData(respons.normalTop.paySortList[type1position].dataList[0].payMoneyList)
//                    rv_tc.scrollToPosition(getIndex(respons.normalTop.paySortList[type1position].dataList[0].payMoneyList))
                    if (getIndex(response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList) > 0 && getIndex(response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList) != response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList.size - 1) {
                        rv_tc.scrollToPosition(getIndex(response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList) - 1)
                    } else {
                        rv_tc.scrollToPosition(getIndex(response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList))
                    }
                    if (response1!!.normalTop.paySortList[type1position].dataList[0].payMoneyList.size == 0) {
                        rv_tc.visibility = View.GONE
                        ll_type_1_bottom.visibility = View.GONE
                    } else {
                        rv_tc.visibility = View.VISIBLE
                        ll_type_1_bottom.visibility = View.VISIBLE
                    }
                } else {
                    rv_tc.visibility = View.GONE
                    ll_type_1_bottom.visibility = View.GONE
                    val emptyView = LayoutInflater.from(context).inflate(R.layout.item_empty_deposit, null, false)
                    type2Adapter!!.emptyView = emptyView
                    adapter?.setNewData(arrayListOf())
                }
            } else {
                rv_tc.visibility = View.GONE
                ll_type_1_bottom.visibility = View.GONE
                val emptyView = LayoutInflater.from(context).inflate(R.layout.item_empty_deposit, null, false)
                type2Adapter!!.emptyView = emptyView
            }

            type3position = getIndex(respons.normalTop.paySortList[type1position].dataList[0].payMoneyList)
            showPrice(respons)
        } else {//卡密
            response1 = respons
            tv_user_id.text = "ID:${respons.kaMi.userId}"

            if (respons.kaMi.vipTime == "未開通VIP特權") {
                img_vip.visibility = View.GONE
                tv_vip_time.text = "未開通VIP特權"
            } else {
                img_vip.visibility = View.VISIBLE
                val msp = SpannableString(respons.kaMi.vipTime)
                if (msp.length < 10) {
                    return
                }
                msp.setSpan(ForegroundColorSpan(resources.getColor(R.color.color_F4C983)), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_vip_time.text = msp
            }
            //刷新頂部套餐數據
//            adapter?.position = kmDefaultSelect
            adapter?.setNewData(respons.kaMi.payMoneyList)
//            rv_tc.scrollToPosition(getIndex(respons.kaMi.payMoneyList))
            if (getIndex(respons.kaMi.payMoneyList)>0&&getIndex(respons.kaMi.payMoneyList)!=respons.kaMi.payMoneyList.size-1){
                rv_tc.scrollToPosition(getIndex(respons.kaMi.payMoneyList)-1)
            }else{
                rv_tc.scrollToPosition(getIndex(respons.kaMi.payMoneyList))
            }
            lxrAdapter?.setNewData(respons.kaMi.touchList)
        }

        if (respons.isPopup == 1) {
            HomeHintDialog.newIntance()
                    .setType(2)
                    .setTopDesc("溫馨提示")
                    .setContent1("充值前請綁定手機，手機是用於找回賬號的唯一憑證。")
                    .invoke(object : HomeHintDialog.ClickListener {
                        override fun click(v: View?) {
                            start(BingdingPhoneFragment.newInstance(isBind, phone, qrcode, homeUrl))
                        }
                    })
                    .show(fragmentManager!!, "show_deposit_hint")
        }

    }

    /**
     * 根据list遍历 获取option为1 的 默认选项的下标
     */
    fun getIndex(list: MutableList<PublicData.PayMoneyData>): Int {
        var index = 0
        if (!CommonUtil.isNull(list)) {
            for (i in 0 until list.size) {
                if (list[i].option == 1) {
                    index = i
                }
            }
        }
        return index

    }


    /**
     * 充值数据回调
     */
    override fun getPayUrl(string: String) {
        val bean = GSONUtil.getEntity(string, PayBean::class.java)
        isReQuestPay = true
        if (bean.code == "1") {
            /*if (UrlCheckUtils.checkUrlIsEffective(bean.redirectUrl)){
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(bean.redirectUrl))))
            }else
                showToast("支付鏈接錯誤，請聯繫客服")*/
            orderNo = bean.orderNo
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bean.redirectUrl)))
        } else {
            LogUtil.loge(bean.message)
            activity!!.runOnUiThread {
                showToast(bean.message)
            }
        }
    }


    override fun getSuccess(response: Api.ExchageKaMiResponse) {
        showToast(response.result.msg)
        pop()
    }

    @SuppressLint("SetTextI18n")
    fun showPrice(respons: Api.GetPayDataResponse) {
        if (respons.normalTop.paySortList.size > type1position &&
                respons.normalTop.paySortList[type1position].dataList.size > type2position
                && respons.normalTop.paySortList[type1position].dataList[type2position].payMoneyList.size > type3position) {
            tv_sure.text = "立即支付${respons.normalTop.paySortList[type1position].dataList[type2position].payMoneyList[type3position].topUpPrice}元"
            if (respons.normalTop.paySortList[type1position].dataList[type2position].payMoneyList[type3position].originalPrice != 0L) {
                tv_money.visibility = View.GONE
                tv_money.text = "已節省${sub(respons.normalTop.paySortList[type1position].dataList[type2position].payMoneyList[type3position].originalPrice, respons.normalTop.paySortList[type1position].dataList[type2position].payMoneyList[type3position].topUpPrice.toLong())}元"
            } else {
                tv_money.visibility = View.GONE
            }
        } else {
            tv_sure.text = "立即支付0元"
        }
    }

    private fun sub(a: Long, b: Long): Long {
        return a - b
    }

    override fun getUserInfoSucc(data: PublicData.ApiUserData) {
        mchatUrl = data.chatUrl
        qrcode = data.key
        homeUrl = data.homeUrl
        phone = data.mobile
        isBind = data.isBind == 1
        Glide.with(activity!!).load(data.headImg).into(img_avatar)
        mPresenter.getPayDataResponse()
    }

    /**
     * type   1是支付方式1  2是支付方式2
     */
    fun showType(type: Int) {
        if (type == 1) {
            ll_type1.visibility = View.VISIBLE
            ll_type_1_bottom.visibility = View.VISIBLE
            ll_type2.visibility = View.GONE
            ll_type_2_bottom.visibility = View.GONE
        } else {
            ll_type1.visibility = View.GONE
            ll_type_1_bottom.visibility = View.GONE
            ll_type2.visibility = View.VISIBLE
            ll_type_2_bottom.visibility = View.VISIBLE
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ll_type_1_bottom -> {
                val typebean = type1Adapter?.data!![type1position] as PublicData.IntData
                if (typebean.`val` == 4) {//人工跳轉到聊天頁面
                    start(WebViewFragment.newIntance("客服", mchatUrl))
                } else {
                    if (adapter?.data!!.size == 0 || type2Adapter?.data!!.size == 0) {
                        showToast("没有充值通道")
                    } else {
                        val bean = type2Adapter?.data!![type2position] as PublicData.PayTypeData
                        val bean1 = adapter?.data!![type3position] as PublicData.PayMoneyData
                        if (TextUtils.isEmpty(bean.orderUrl)) {
                            showToast("充值渠道鏈接為空")
                        } else {
                            val url = bean.orderUrl + "?userId=${response1!!.normalTop.userId}&payMoneyId=${bean1.payMoneyId}"
                            finishUrl = bean.isPayUrl
                            mPresenter.getPayUrl(url, 1, "")
                        }

                    }
                }

            }
            R.id.tv_sure2 -> {
                if (TextUtils.isEmpty(edt_psd.text.toString())) {
                    showToast("請輸入卡密")
                    return
                }
                mPresenter.exchageKaMiReqeust(edt_psd.text.toString())
            }
        }
    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        var count = 0
        runnable = object : Runnable {
            override fun run() {
                //do something
                count++
                if (isReQuestPay) {
                    if (count == 30) {
                        return
                    }
                } else {
                    return
                }
                LogUtil.loge("当前count$count")
                mPresenter.getPayUrl(finishUrl, 2, orderNo)
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000) // 开始Timer
    }


    /**
     * 充值完成后回调
     */
    override fun getPayFinish(string: String) {
        val bean = GSONUtil.getEntity(string, PayFinishBean::class.java)
        if (bean.status == 1) {
            if (bean.payStatus == 1) {
                isReQuestPay = false
                activity!!.runOnUiThread {
                    showToast(bean.message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) //停止Timergit
    }

}