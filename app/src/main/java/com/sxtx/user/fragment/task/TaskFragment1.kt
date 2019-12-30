package com.sxtx.user.fragment.task

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.like.base.base.BaseFragment
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.adapter.TaskAdapter1
import com.sxtx.user.mvp.presenter.main.TaskPresenter
import com.sxtx.user.mvp.view.main.ITaskView
import kotlinx.android.synthetic.main.fragment_task_1.*

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class TaskFragment1 : BaseFragment<TaskPresenter>(),ITaskView {
    override fun getQrCodeSucc(response: Api.GetQrCodeResponse?) {

    }

    var adapter: TaskAdapter1? = null
    override fun getResId(): Any {
        return R.layout.fragment_task_1
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_task_1.layoutManager = linearLayoutManager
        adapter = TaskAdapter1()
        rv_task_1.adapter = adapter
        adapter?.invoke(object : TaskAdapter1.ClickListener{
            override fun click(dataId: Long) {
                mPresenter.getTaskAward(1,dataId)
            }
        })

        refresh_layout.setOnRefreshListener {
            mPresenter.getTask()
        }
        refresh_layout.setEnableLoadMore(false)
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        mPresenter.getTask()
    }

    override fun getTaskSucc(list: MutableList<PublicData.InviteData>?) {
        refresh_layout.finishRefresh()
        adapter?.setNewData(list)
    }

    override fun getTaskCommonSucc(list: MutableList<PublicData.CommomData>?) {

    }


}