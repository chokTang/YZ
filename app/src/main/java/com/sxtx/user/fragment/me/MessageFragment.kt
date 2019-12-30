package com.sxtx.user.fragment.me

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.adapter.MessageAdapter
import com.sxtx.user.model.bean.RecordBean
import com.sxtx.user.mvp.presenter.me.MessagePresenter
import com.sxtx.user.mvp.view.me.IMessageView
import event.HeartServiceEvents
import kotlinx.android.synthetic.main.fragment_message.*
import org.greenrobot.eventbus.EventBus

class MessageFragment : BaseStatusToolbarFragment<MessagePresenter>() , IMessageView {


    var adapter: MessageAdapter? = null
    val list: MutableList<PublicData.ApiUserMessage> = arrayListOf()

    override fun getMainResId(): Int {
        return R.layout.fragment_message
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        onBack()
        setToolbarTitle("消息")
        showView(BaseFragment.MAIN_VIEW)



        val linearLayoutManager = LinearLayoutManager(activity)
        rv_record.layoutManager = linearLayoutManager
        adapter = MessageAdapter()
        rv_record.adapter = adapter


    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mPresenter.getClickMyMessageReqeust(true)
    }


    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)

        refresh_layout.setOnRefreshListener {
            mPresenter.getClickMyMessageReqeust( true)
        }
        refresh_layout.setOnLoadMoreListener {
            mPresenter.getClickMyMessageReqeust(false)
        }

        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter?.data!![position] as RecordBean
            when (view.id) {
                R.id.ll_select -> {
                    bean.isSelected = !bean.isSelected

                    adapter.notifyItemChanged(position, bean)
                }

            }

        }


    }


    override fun getClickMyMessageResponseData(refresh:Boolean,videoList: List<PublicData.ApiUserMessage>) {
        if (refresh){
            refresh_layout.finishRefresh()
            this.list.clear()
        }else{
            refresh_layout.finishLoadMore()
        }
        if (videoList.size > 0) {
            this.list.addAll(videoList)
        }
        if (this.list.size == 0) {//没有数据
            adapter!!.setNewData(this.list)
            val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
            adapter!!.emptyView = emptyView
        } else {//有数据
            adapter!!.setNewData(this.list)
        }
        EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.TO_REFRESH,null))
    }
}

