package com.sxtx.user.fragment.task

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.like.base.base.BaseFragment
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.adapter.TaskAdapter2
import com.sxtx.user.mvp.presenter.main.TaskPresenter
import com.sxtx.user.mvp.view.main.ITaskView
import kotlinx.android.synthetic.main.fragment_task_2.*

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class TaskFragment2 : BaseFragment<TaskPresenter>(), ITaskView {
    override fun getQrCodeSucc(response: Api.GetQrCodeResponse?) {

    }

    var adapter2:TaskAdapter2? = null
    override fun getResId(): Any {
        return R.layout.fragment_task_2
    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        mPresenter.getTask2()
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        var linearLayoutManager = LinearLayoutManager(activity)
        rv_task_2.layoutManager = linearLayoutManager
        adapter2 = TaskAdapter2()
        rv_task_2.adapter = adapter2
        adapter2?.invoke(object : TaskAdapter2.ClickListener{
            override fun click(dataId: Long) {
                mPresenter.getTaskAward(2,dataId)
            }
        })
        refresh_layout.setOnRefreshListener {
            mPresenter.getTask2()
        }
        refresh_layout.setEnableLoadMore(false)
    }

    override fun getTaskCommonSucc(list: MutableList<PublicData.CommomData>?) {
        refresh_layout.finishRefresh()
        adapter2?.setNewData(list)
    }

    override fun getTaskSucc(list: MutableList<PublicData.InviteData>?) {

    }
}