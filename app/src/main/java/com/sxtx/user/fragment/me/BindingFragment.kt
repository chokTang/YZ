package com.sxtx.user.fragment.me

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.sxtx.user.R
import com.sxtx.user.mvp.presenter.me.BindingPresenter
import com.sxtx.user.mvp.view.me.IBindingView
import kotlinx.android.synthetic.main.fragment_binding_yqm.*

/**
 *
 * 介紹:
 * 作者:CHOK
 */

class BindingFragment : BaseStatusToolbarFragment<BindingPresenter>(),IBindingView,View.OnClickListener {
    override fun bindSucc() {
        pop()
    }

    override fun getMainResId(): Int {
        return R.layout.fragment_binding_yqm
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)

        onBack()
        setToolbarTitle("綁定邀請碼")
        showView(BaseFragment.MAIN_VIEW)
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        setListener()
    }

    fun setListener(){
        tv_binding.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_binding->{
                val inviteCode=edt_yqm.text.toString().trim();
                if (TextUtils.isEmpty(inviteCode)){
                    showToast("邀請碼不能為空")
                     return
                }
                mPresenter.inviteCodeReqeust(inviteCode)
            }
        }
    }

}