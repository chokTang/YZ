package com.sxtx.user.fragment.me

import android.os.Bundle
import android.view.View
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.like.utilslib.app.AppUtil
import com.sxtx.use.GestureFragment
import com.sxtx.user.R
import com.sxtx.user.mvp.presenter.me.SetPresenter
import com.sxtx.user.mvp.view.me.ISetView
import com.sxtx.user.util.AppPreference
import event.HeartServiceEvents
import kotlinx.android.synthetic.main.fragment_set.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * 介紹: 設置
 * 作者:CHOK
 */
class SetFragment : BaseStatusToolbarFragment<SetPresenter>(), View.OnClickListener , ISetView {


    var psdSelectd = false
    var playSelectd = false

    override fun getMainResId(): Int {
        return R.layout.fragment_set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        onBack()
        setToolbarTitle("設置")

        setListener()
        initView()
        showView(BaseFragment.MAIN_VIEW)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mPresenter.AutoPlayReqeust()
    }

    private fun initView() {
        if(AppPreference.getIntance().getIsGeSture()){
            psdSelectd=true
            img_psd_switch.isSelected = true
        }
     }



    fun setListener() {
        img_psd_switch.setOnClickListener(this)
        img_play_switch.setOnClickListener(this)
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        tv_version_code.text = "V${AppUtil.getVersionName()}"
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.img_psd_switch -> {
              if(psdSelectd){
                    start(GestureFragment().newInstance(true,true))
                }else{
                    start(GestureFragment().newInstance(false,false))

                }
            }
            R.id.img_play_switch -> {
                playSelectd = !playSelectd
                img_play_switch.isSelected = playSelectd
                mPresenter.ModifyAutoPlayReqeust()
            }
        }
    }

    override fun AutoPlayReqeustSucceed(position: Int) {
        if (position==1){
            playSelectd = true
        }else if (position==2){
            playSelectd = false
        }
        img_play_switch.isSelected = playSelectd
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventBusHandle(message: HeartServiceEvents) {
        when (message.getEventId()) {
            HeartServiceEvents.CANCLE_GESTURE_PASSWORD -> {
                psdSelectd = false
                img_psd_switch.isSelected = false
                AppPreference.getIntance().setIsGeSture(false)
            }
            HeartServiceEvents.SURE_GESTURE_PASSWORD -> {
                psdSelectd = true
                img_psd_switch.isSelected = true
                AppPreference.getIntance().setIsGeSture(true)
           }
        }
    }
}