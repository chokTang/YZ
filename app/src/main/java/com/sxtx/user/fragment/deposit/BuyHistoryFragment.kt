package com.sxtx.user.fragment.deposit

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.adapter.BuyHistoryAdapter
import com.sxtx.user.mvp.presenter.deposit.BuyHistoryPresonter
import com.sxtx.user.mvp.view.IBuyHistoryView
import kotlinx.android.synthetic.main.fragment_buy_history.*

class BuyHistoryFragment : BaseStatusToolbarFragment<BuyHistoryPresonter>(), IBuyHistoryView {
    var list: MutableList<PublicData.RechargeRecordData> = arrayListOf()
    override fun getDataSucc(refresh: Boolean, list: MutableList<PublicData.RechargeRecordData>, total: Long, page: Int) {
        if (refresh) {//刷新
            refresh_layout.finishRefresh()
            this.list.clear()
            this.list.addAll(list)
        } else {//加载
            refresh_layout.finishLoadMore()
            this.list.addAll(list)
            if (total == page.toLong()) {//添加完数据
                refresh_layout.finishLoadMoreWithNoMoreData()
            }
        }
        if (this.list.size == 0) {//没有数据
            adapter!!.setNewData(this.list)
            val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
            adapter!!.emptyView = emptyView
        } else {//有数据
            adapter!!.setNewData(this.list)
        }
    }

    var adapter: BuyHistoryAdapter? = null
    override fun getMainResId(): Int {
        return R.layout.fragment_buy_history
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        onBack()
        setToolbarTitle("購買記錄")
        showView(BaseFragment.MAIN_VIEW)
        adapter = BuyHistoryAdapter()
        val layoutManager = LinearLayoutManager(_mActivity)
        rv_buyhistory.layoutManager = layoutManager
        rv_buyhistory.adapter = adapter
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        refresh_layout.setOnRefreshListener {
            mPresenter.getData(true)
        }
        refresh_layout.setOnLoadMoreListener {
            mPresenter.getData(false)
        }
        refresh_layout.autoRefresh()
    }
}