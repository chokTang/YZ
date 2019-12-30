package com.sxtx.user.fragment.update

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.like.base.dialog.BaseDialogFragment
import com.like.utilslib.UtilApp
import com.like.utilslib.screen.ScreenUtil
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.sxtx.user.R
import com.sxtx.user.mvp.presenter.main.HomePresenter
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.QrCodeUtil
import kotlinx.android.synthetic.main.dialog_me_certificate.*
import java.io.File
import java.io.FileOutputStream


/**
 * 我的凭证
 * Created by Administrator on 2018/3/13.
 */

class CertificateDialog : BaseDialogFragment<HomePresenter>(),View.OnClickListener {

    var mcontent:String = "無效憑證"
    var mStrGw:String = "youzi.com"

    companion object {
        fun newIntance(): CertificateDialog {
            val dialog = CertificateDialog()
            return dialog
        }
    }

    override fun getResId(): Any {
        return R.layout.dialog_me_certificate
    }

    override fun initView() {
        img_qrcode.setImageBitmap(QrCodeUtil.createQRCodeBitmap(mcontent, DensityUtil.dp2px(105f), DensityUtil.dp2px(105f)))
        tv_gw.text = mStrGw
        tv_save.setOnClickListener(this)
    }

    fun setContent(content:String):CertificateDialog{
        mcontent = content
        return this
    }

    fun setGw(gw:String):CertificateDialog{
        mStrGw = gw
        return this
    }

    override fun initData() {
    }

    override fun getViewWidth(): Int {
        return (ScreenUtil.getScreenWidth() * 0.7).toInt()
    }

    override fun getViewHeight(): Int {
        return -2
    }

    override fun getViewGravity(): Int {
        return Gravity.CENTER
    }

    override fun getAnimationType(): Int {
        return FORM_BOTTOM_TO_TOP
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_save->{
                saveImage(rl_qrcode)
                dismiss()
            }
        }
    }

    /**
     * 保存圖片到本地
     */
    fun saveImage(view: View) {
        view.isDrawingCacheEnabled = true
        view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        view.drawingCacheBackgroundColor = Color.WHITE
        val cachebmp = loadBitmapFromView(view)
        val fos: FileOutputStream
        var fileName = ""
        val file: File
        try {
            // 判断手机设备是否有SD卡
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // SD卡根目录
                val appDir = File(Environment.getExternalStorageDirectory(), "Youzi")
                if (!appDir.exists()) {
                    appDir.mkdir()
                }
                fileName = System.currentTimeMillis().toString() + ".jpg"
                file = File(appDir, fileName)
                fos = FileOutputStream(file)
            } else
                throw Exception("创建文件失败!")

            cachebmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            /**
             * 通知刷新相册  MediaStore.Images.Media.insertImage(activity!!.contentResolver, file.getAbsolutePath(), fileName, null)（低版本无效，4.4插入不了）
             * 或者采用发送广播的方式
             */
             val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
             val uri = Uri.fromFile(file)
             intent.data = uri
             activity!!.sendBroadcast(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val toast = Toast.makeText(UtilApp.getIntance().applicationContext, "已保存", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
        view.destroyDrawingCache()
        AppPreference.getIntance().setSaveCertificate(true)
    }

    /**
     * 把一个View转换成图片
     */
    fun loadBitmapFromView(v: View): Bitmap {
        val w = v.width
        val h = v.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        /** 如果不设置canvas画布为白色，则生成透明  */
        v.layout(0, 0, w, h)
        v.draw(c)
        return bmp
    }
}

