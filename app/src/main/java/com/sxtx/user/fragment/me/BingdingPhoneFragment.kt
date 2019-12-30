package com.sxtx.user.fragment.me

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.like.utilslib.other.RegexUtil
import com.sxtx.user.R
import com.sxtx.user.fragment.update.CertificateDialog
import com.sxtx.user.mvp.presenter.me.BingdingPhonePresenter
import com.sxtx.user.mvp.view.me.IBingdingPhoneView
import com.sxtx.user.util.AppPreference
import event.HeartServiceEvents
import kotlinx.android.synthetic.main.fragment_bingding_phone.*
import org.greenrobot.eventbus.EventBus

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BingdingPhoneFragment : BaseStatusToolbarFragment<BingdingPhonePresenter>(),IBingdingPhoneView,View.OnClickListener {
    var isBind = false
    var phone = ""
    var qrcode = ""
    var homeUrl = ""
    override fun bingdSucc() {
        AppPreference.getIntance().isBindPhone = true
        EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.TO_RETURN,null))
        pop()
    }

    companion object{
        const val KEY_BIND = "isBind"
        const val KEY_PHONE = "phone"
        const val KEY_QRCODE = "qrcode"
        const val KEY_HOMEURL = "homeurl"
        fun newInstance(isBind: Boolean,phone :String,qrcode :String,homeurl :String): BingdingPhoneFragment {
            val args = Bundle()
            args.putBoolean(KEY_BIND, isBind)
            args.putString(KEY_PHONE, phone)
            args.putString(KEY_QRCODE, qrcode)
            args.putString(KEY_HOMEURL, homeurl)
            val fragment = BingdingPhoneFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_bingding -> {
                if (TextUtils.isEmpty(edt_account.text.toString())){
                    showToast("請輸入手機號碼")
                    return
                }
                if (!RegexUtil.checkMobile(edt_account.text.toString())){
                    showToast("請輸入正確的手機號碼")
                    return
                }
                if (TextUtils.isEmpty(edt_psd.text.toString())){
                    showToast("請輸入密碼")
                    return
                }
                mPresenter.bingdingPhone(edt_account.text.toString(),edt_psd.text.toString())
            }
            R.id.tv_certificate -> {
                CertificateDialog.newIntance()
                        .setContent(qrcode)
                        .setGw(homeUrl)
                        .show(childFragmentManager, "show_me_certificate")
            }
        }

    }

    override fun getMainResId(): Int {
        return R.layout.fragment_bingding_phone
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        onBack()
        var bundle = arguments
        isBind = bundle?.getBoolean(KEY_BIND,false)!!
        phone = bundle.getString(KEY_PHONE)
        qrcode = bundle.getString(KEY_QRCODE)
        homeUrl = bundle.getString(KEY_HOMEURL)
        checkBind()
        setToolbarTitle("綁定手機")
        showView(BaseFragment.MAIN_VIEW)
        tv_bingding.setOnClickListener(this)
        tv_certificate.setOnClickListener(this)
    }

    private fun checkBind() {
        if (isBind || AppPreference.getIntance().isBindPhone){
            ll_pass.visibility = View.GONE
            line_pass.visibility = View.GONE
            tv_bingding.setBackgroundResource(R.drawable.bg_grey_21)
            tv_bingding.isEnabled = false
            edt_account.setText(phone)
            edt_account.isEnabled = false
            tv_bingding.text = "已綁定"
        }else{
            ll_pass.visibility = View.VISIBLE
            line_pass.visibility = View.VISIBLE
            tv_bingding.setBackgroundResource(R.drawable.bg_theme_21)
            tv_bingding.isEnabled = true
            edt_account.isEnabled = true
            tv_bingding.text = "綁定"
        }
    }
}