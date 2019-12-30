package com.sxtx.user.fragment.update

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.like.base.dialog.BaseDialogFragment
import com.like.utilslib.screen.ScreenUtil
import com.sxtx.user.R
import com.sxtx.user.mvp.presenter.main.HomePresenter
import kotlinx.android.synthetic.main.dialog_home_hint.*


/**
 * 公告
 * Created by Administrator on 2018/3/13.
 */

class HomeHintDialog : BaseDialogFragment<HomePresenter>(),View.OnClickListener {

    var mcontent:String = ""
    var mcontent1:String = ""
    var mTitle:String = ""
    var mTopDesc:String = ""
    var mType:Int = 1

    companion object {
        fun newIntance(): HomeHintDialog {
            val dialog = HomeHintDialog()
            return dialog
        }
    }

    override fun getResId(): Any {
        return R.layout.dialog_home_hint
    }

    override fun initView() {
        if (TextUtils.isEmpty(mcontent)&&!TextUtils.isEmpty(mcontent1)){
            tv_content.visibility = View.GONE
            tv_content1.visibility = View.VISIBLE
        }else if (!TextUtils.isEmpty(mcontent)&&TextUtils.isEmpty(mcontent1)){
            tv_content.visibility = View.VISIBLE
            tv_content1.visibility = View.GONE
        }
        tv_content.text = mcontent
        tv_content1.text = mcontent1
        tv_title.text = mTitle
        tv_top_desc.text = mTopDesc
        if (mType==1){
            ll_type_2.visibility = View.GONE
            tv_confirm.visibility = View.VISIBLE
        }else{
            ll_type_2.visibility = View.VISIBLE
            tv_confirm.visibility = View.GONE
        }
        tv_confirm.setOnClickListener(this)
        tv_bingding.setOnClickListener(this)
        tv_see.setOnClickListener(this)
    }


    /**
     * 初始化回调
     */
    operator fun invoke(clickListener: ClickListener?): HomeHintDialog {
        this.clickListener = clickListener
        return this
    }


    fun setContent(content:String):HomeHintDialog{
        mcontent = content
        return this
    }

    fun setContent1(content:String):HomeHintDialog{
        mcontent1 = content
        return this
    }

    fun setTitle(title:String):HomeHintDialog{
        mTitle = title
        return this
    }

    fun setTopDesc(topDesc:String):HomeHintDialog{
        mTopDesc = topDesc
        return this
    }

    fun setType(type:Int):HomeHintDialog{
        mType = type
        return this
    }

    override fun initData() {
    }

    override fun getViewWidth(): Int {
        return (ScreenUtil.getScreenWidth() * 0.6).toInt()
    }

    override fun getViewHeight(): Int {
        return -2
    }

    override fun getViewGravity(): Int {
        return Gravity.CENTER
    }

    override fun getAnimationType(): Int {
        return FORM_LEFT_TO_RIGHT
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_confirm->{
                clickListener?.click(v)
                dismiss()
            }
            R.id.tv_bingding->{
                clickListener?.click(v)
                dismiss()
            }
            R.id.tv_see->{
                dismiss()
            }
        }
    }

    interface ClickListener {
        fun click(v: View?)
    }

    var clickListener: ClickListener? = null


}

