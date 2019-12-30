package com.sxtx.user.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.sxtx.user.R
import com.sxtx.user.dialog.CommDialog


/**
 * 搜索播放
 * 一个全透明的activity
 */

class AllCheckActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_all_check)
        CommDialog.newInstance(this).setTitle("提示")
                .setContent("似乎已经断开网络连接")
                .setLeftText("关闭")
                .setRightText("检查网络")
                .setClickListen(object :CommDialog.TwoSelDialog{
                    override fun leftClick() {
                        finish()
                    }

                    override fun rightClick() {
                        tell()
                        finish()
                    }

                }).show()
    }

    //   第一个参为包名，第二个各个设置的类名(可以参考下面，包名不用改变)
    private fun tell() {
        startActivity(Intent(Settings.ACTION_SETTINGS))
    }

}
