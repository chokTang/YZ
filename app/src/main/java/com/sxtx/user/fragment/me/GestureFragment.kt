package com.sxtx.use




import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.like.utilslib.app.AppUtil
import com.sxtx.user.R
import com.sxtx.user.mvp.presenter.me.GesturePresenter
import com.sxtx.user.util.AppPreference
import event.HeartServiceEvents
import kotlinx.android.synthetic.main.fragment_gesture.*
import kotlinx.android.synthetic.main.fragment_set.*
import org.greenrobot.eventbus.EventBus

class GestureFragment : BaseStatusToolbarFragment<GesturePresenter>(), View.OnClickListener, com.sxtx.user.widget.gesture.TraceListener {


    override fun onTraceUnFinish() {
        tv_error_text.visibility=View.VISIBLE
    }
    var firstPassword=""


    fun newInstance(isGesture: Boolean,isCancle:Boolean): GestureFragment {

        val args = Bundle()
        args.putBoolean("isGesture", isGesture)
        args.putBoolean("isCancle", isCancle)
        val fragment = GestureFragment()
        fragment.arguments = args
        return fragment
    }


    override fun onTraceFinished(traceCode: String?) {
        if (arguments!!.getBoolean("isGesture", false)&&!TextUtils.isEmpty(AppPreference.getIntance().getGeSture())) {
            if (TextUtils.equals(AppPreference.getIntance().getGeSture(),traceCode)){
                if (arguments!!.getBoolean("isCancle", false)){
                    EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.CANCLE_GESTURE_PASSWORD,null) )
                    pop()
                }else{
                    activity?.finish()
                }
           }else{
                showToast("密碼錯誤")
            }
        }else{
            if (traceCode?.length!! <6){
                tv_error_text.visibility=View.VISIBLE
            }else{
                tv_error_text.visibility=View.GONE
                if (TextUtils.isEmpty(firstPassword)){
                    firstPassword= traceCode;
                    tv_title.text=" 確認解鎖密碼"
                    tv_restart_gesture.visibility=View.VISIBLE
                    showToast("請再次滑動確認密碼")
                }else{
                    if (TextUtils.equals(firstPassword,traceCode)){
                        EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.SURE_GESTURE_PASSWORD,null) )
                        AppPreference.getIntance().setGeSture(traceCode)
                        super.pop()
                    }else{
                        showToast("與上次繪製不一致，請重新繪製")
                    }
                }
            }
        }

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_restart_gesture ->{
                firstPassword=""
                tv_title.text=" 請繪製手勢解鎖"
                tv_error_text.visibility=View.GONE
                view_gesture.reBuild()
            }
        }
    }



    override fun getMainResId(): Int {
        return R.layout.fragment_gesture
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        view_gesture.traceListener=this
        setListener()
        showView(BaseFragment.MAIN_VIEW)
        if (arguments!!.getBoolean("isGesture", false)) {
            if(arguments!!.getBoolean("isCancle", false)){
                setCallBack()
                tv_title.text=" 請繪製手勢解鎖"
            }else{
                tv_title.visibility=View.VISIBLE
                tv_title.text="繪製解鎖圖案"
                tv_restart_gesture.visibility=View.INVISIBLE
            }
          }else{
           setCallBack()
        }
    }

    fun setCallBack(){
        onBack()
        setToolbarTitle("")
        tv_title.visibility=View.VISIBLE
        tv_restart_gesture.visibility=View.INVISIBLE
    }

    fun setListener() {
        tv_restart_gesture.setOnClickListener(this)
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        tv_version_code?.text = "V:${AppUtil.getVersionName()}"
    }


    override fun onDestroy() {
        super.onDestroy()
    }



}