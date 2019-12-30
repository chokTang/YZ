package com.sxtx.user.fragment.update

import android.graphics.drawable.AnimationDrawable
import android.view.Gravity
import com.like.base.dialog.BaseDialogFragment
import com.sxtx.user.R
import com.sxtx.user.mvp.presenter.main.HomePresenter
import kotlinx.android.synthetic.main.dialog_http_request.*


/**
 * 开启时候网络请求提示
 */

class HttpRequestDialog : BaseDialogFragment<HomePresenter>() {


    companion object {
        fun newIntance(): HttpRequestDialog {
            val dialog = HttpRequestDialog()
            return dialog
        }
    }

    override fun getResId(): Any {
        return R.layout.dialog_http_request
    }

    override fun initView() {
        val animationDrawable = dialog_iv.background as AnimationDrawable
        animationDrawable.start()
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun initData() {
    }

    override fun getViewWidth(): Int {
        return -2
    }

    override fun getViewHeight(): Int {
        return -2
    }

    override fun getViewGravity(): Int {
        return Gravity.CENTER
    }

    override fun getAnimationType(): Int {
        return CNTER
    }



}

