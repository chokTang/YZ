package com.sxtx.user.fragment.task

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.like.base.adapter.rvhelper.DividerItemDecoration
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.sxtx.user.R
import com.sxtx.user.adapter.InviteAdapter
import com.sxtx.user.model.bean.InviteBean
import com.sxtx.user.mvp.presenter.task.InvitePresenter
import kotlinx.android.synthetic.main.fragment_invit_method.*

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class InviteFragment : BaseStatusToolbarFragment<InvitePresenter>() ,View.OnClickListener{


    var adapter: InviteAdapter? = null
    var list: MutableList<InviteBean> = arrayListOf()

    override fun getMainResId(): Int {
        return R.layout.fragment_invit_method
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(mToolbar).init()
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        onBack()
        setToolbarTitle("邀請方法")

        showView(BaseFragment.MAIN_VIEW)
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_invite.layoutManager = linearLayoutManager
        adapter = InviteAdapter()
        rv_invite.adapter = adapter
        rv_invite.addItemDecoration(DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, R.drawable.listdivider_gray_13))
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        setListener()
        for (i in 0 until 6) {
            val bean = InviteBean()
            bean.type = i
            list.add(bean)
        }
        adapter?.setNewData(list)
    }

    fun setListener(){
        tv_btn.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_btn->{
                //返回
                pop()
            }
        }
    }
}