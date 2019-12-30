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
import com.sxtx.user.adapter.CollectionAdapter
import com.sxtx.user.model.bean.RecordBean
import com.sxtx.user.mvp.presenter.me.CollectionPresenter
import com.sxtx.user.mvp.view.me.ICollectionView
import kotlinx.android.synthetic.main.fragment_record.*

/**
 *
 * 介紹: 收藏
 * 作者:CHOK
 */
class CollectionFragment : BaseStatusToolbarFragment<CollectionPresenter>() , ICollectionView {



    var adapter: CollectionAdapter? = null
    val list: MutableList<RecordBean> = arrayListOf()

    override fun getMainResId(): Int {
        return R.layout.fragment_record
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        onBack()
        setToolbarTitle("收藏")
        showView(BaseFragment.MAIN_VIEW)
        ll_edit.visibility = View.GONE


        val linearLayoutManager = LinearLayoutManager(activity)
        rv_record.layoutManager = linearLayoutManager
        adapter = CollectionAdapter()
        rv_record.adapter = adapter
        rv_record.addItemDecoration(DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, R.drawable.listdivider_white_16))

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mPresenter.getCheckCollectRecrdReqeust()
    }


    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)



        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter?.data!![position] as RecordBean
            when(view.id){
                R.id.ll_select->{
                    bean.isSelected = !bean.isSelected
                    adapter.notifyItemChanged(position,bean)
                }
                R.id.ll_collection->{
                   if (!bean.isCollected)mPresenter.ClickCollectReqeust(position,bean.videoId)
                    else mPresenter.DeleteCollectRecrdReqeust(position,bean.videoId)
          }
            }
        }
        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter?.data!![position] as RecordBean
//            start(VideoPlayFragment.newIncetance(bean.videoId))
            startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID,bean.videoId))

        }

        refresh_layout.setOnRefreshListener {
            mPresenter.getCheckCollectRecrdReqeust()
        }


    }


    override fun getCheckCollectRecrdReqeustData(videoList: List<PublicData.ClassifyVideoData>) {
        refresh_layout.finishRefresh()
        this.list.clear()
        if (videoList.size>0){
            for (element in videoList){
                val recordBean = RecordBean()
                recordBean.setName(element.videoName)
                recordBean.setPic(element.pictureAddress)
                recordBean.setVideoId(element.videoId)
                recordBean.setCollected(element.isCollect==1)
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


    //type 1-当前为收藏 2-当前为没有收藏
    override fun collection(position:Int,type: Int) {
        if (type==1){
          (adapter!!.data[position]).isCollected=true;
         }else{
            (adapter!!.data[position]).isCollected=false;
        }
        adapter!!.position=position
      adapter?.notifyItemChanged(position,(adapter!!.data[position]).isCollected)
    }


    override fun cancleCollection(position: Int) {
        (adapter!!.data[position]).isCollected=false
      /*  adapter!!.data.removeAt(position)
        adapter?.notifyDataSetChanged()*/
        adapter?.removeData(position)
    }


}