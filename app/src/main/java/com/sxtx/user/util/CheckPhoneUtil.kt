package com.sxtx.user.util

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.text.TextUtils
import com.like.utilslib.UtilApp
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class CheckPhoneUtil {

    companion object {
        fun checkCanCall(): Boolean {
            val url = "tel:" + "10086"
            val intent = Intent()
            intent.data = Uri.parse(url)
            intent.action = Intent.ACTION_DIAL
            // 隐示意图跳转到打电话界面 Intent （检查是否存在）
            val canResolverIntent = intent.resolveActivity(UtilApp.getIntance().applicationContext.packageManager) != null
            return canResolverIntent
        }

        /**
         * 判断蓝牙是否有效来判断是否为模拟器
         *
         * @return true 为模拟器
         */
        fun notHasBlueTooth(): Boolean {
            val ba = BluetoothAdapter.getDefaultAdapter()
            return if (ba == null) {
                true
            } else {
                // 如果有蓝牙不一定是有效的。获取蓝牙名称，若为null 则默认为模拟器
                val name = ba.name
                TextUtils.isEmpty(name)
            }
        }


        /**
         * 判断是否存在光传感器来判断是否为模拟器
         * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
         * @return true 为模拟器
         */
        fun notHasLightSensorManager(context: Context): Boolean {
            val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
            val sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //光
            return null == sensor8
        }


        /**
         * 判断cpu是否为电脑来判断 模拟器
         *
         * @return true 为模拟器
         */
        fun checkIsNotRealPhone(): Boolean {
            val cpuInfo = readCpuInfo()
            if ((cpuInfo.contains("intel") || cpuInfo.contains("amd"))) {
                return true
            }
            return false
        }

        fun readCpuInfo(): String {
            var result = ""
            try {
                val args = arrayListOf<String>("/system/bin/cat", "/proc/cpuinfo")
                val cmd = ProcessBuilder(args)
                val process = cmd.start()
                val sb = StringBuffer()
                val readLine = ""
                val responseReader = BufferedReader(InputStreamReader(process.inputStream, "utf-8"))
                while (responseReader.readLine()!=null) {
                    sb.append(responseReader.readLine())
                }
                responseReader.close()
                result = sb.toString().toLowerCase()
            } catch (ex: IOException) {
            }
            return result
        }


    }
}