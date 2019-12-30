package com.sxtx.user.fragment.me

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.like.base.adapter.rvhelper.DividerItemDecoration
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.activity.VideoPlayActivity
import com.sxtx.user.adapter.RecordAdapter
import com.sxtx.user.model.bean.RecordBean
import com.sxtx.user.mvp.presenter.me.RecordPresenter
import com.sxtx.user.mvp.view.me.IRecordView
import kotlinx.android.synthetic.main.fragment_record.*

/**
 *
 * 介紹: 觀看記錄
 * 作者:CHOK
 */
class RecordFragment : BaseStatusToolbarFragment<RecordPresenter>(), View.OnClickListener, IRecordView {



    var isAllselect = false  //是否已經全選
    var isEdit = false  //是否是編輯模式下
    var adapter: RecordAdapter? = null
    val list: MutableList<RecordBean> = arrayListOf()
    val vieoIds: MutableList<RecordBean> = arrayListOf()
    override fun getMainResId(): Int {
        return R.layout.fragment_record
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        onBack()
        setToolbarTitle("觀看歷史")
        setToolbarRightTitle("編輯")
        setListener()
        showView(BaseFragment.MAIN_VIEW)
        ll_edit.visibility = View.GONE


        val linearLayoutManager = LinearLayoutManager(activity)
        rv_record.layoutManager = linearLayoutManager
        adapter = RecordAdapter()
        rv_record.adapter = adapter
        rv_record.addItemDecoration(DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, R.drawable.listdivider_white_16))

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mPresenter.getCheckHistroyRecrdReqeust()
    }

    fun setListener() {
        rightTitle.setOnClickListener {
            isEdit = !isEdit
            if (isEdit) {
                ll_edit.visibility = View.VISIBLE
                adapter?.type = 2
            } else {
                ll_edit.visibility = View.GONE
                adapter?.type = 1
            }
            adapter?.notifyDataSetChanged()
        }
        tv_all.setOnClickListener(this)
        tv_delete.setOnClickListener(this)

    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)



        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter?.data!![position] as RecordBean
            when(view.id){
                R.id.ll_select->{
                    bean.isSelected = !bean.isSelected
                }
                R.id.ll_collection->{
                    bean.isSelected = !bean.isCollected
                }
            }
            adapter.notifyItemChanged(position,bean)
        }

        adapter?.setOnItemClickListener { adapter, _, position ->
            val bean = adapter?.data!![position] as RecordBean
//            start(VideoPlayFragment.newIncetance(bean.videoId))
            startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID,bean.videoId))

        }

        refresh_layout.setOnRefreshListener {
            mPresenter.getCheckHistroyRecrdReqeust()
        }


    }

    override fun deleteSucceed() {
           for (element in  vieoIds!!){
                if ((element as RecordBean).isSelected ){
                    this.list.remove(element)
                }
            }
        if (this.list.size == 0){
            adapter!!.setNewData(this.list)
            val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
            adapter!!.emptyView = emptyView
            ll_edit.visibility = View.GONE
        }else{
            adapter!!.setNewData(this.list)
        }
        vieoIds.clear()
    }

    override fun getCheckHistroyRecrdData(list: MutableList<PublicData.ClassifyVideoData>) {
        refresh_layout.finishRefresh()
        this.list.clear()
        if (list.size>0){
            for (element in list){
                val recordBean = RecordBean()
                recordBean.setName(element.videoName)
                recordBean.setPic(element.pictureAddress)
                recordBean.setVideoId(element.videoId)
                recordBean.setIsFreeOfCharge(element.isFreeOfCharge)
                recordBean.setTime(element.videoTime)
                recordBean.setUptime(element.upTime)
                recordBean.setGiveMark(element.giveMark)
                recordBean.setPlayNum(element.playNum)
                recordBean.setIsFreeOfCharge(element.isFreeOfCharge)
                this.list.add(recordBean)
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



    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_all -> {
                isAllselect = !isAllselect
                for (i in 0 until  adapter?.data!!.size){
                    (adapter!!.data[i] as RecordBean).isSelected = isAllselect
                }
                adapter!!.notifyDataSetChanged()
            }
            R.id.tv_delete -> {
                getDeleteVieoIds();
                mPresenter.deleteHistroyRecrdReqeust(vieoIds)
                //showToast("你點擊了刪除")
            }
        }
    }

    fun getDeleteVieoIds(){
        for (i in 0 until  adapter?.data!!.size){
            if ((adapter!!.data[i] as RecordBean).isSelected ){
                vieoIds.add(adapter!!.data[i])
            }
        }
    }

}