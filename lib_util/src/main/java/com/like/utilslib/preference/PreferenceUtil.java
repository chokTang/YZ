package com.like.utilslib.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.like.utilslib.UtilApp;
import com.like.utilslib.other.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @author longshao
 * 数据的分享 保存到数据下的东西 保存到本地目录下
 */
public class PreferenceUtil {

    private SharedPreferences mPerferences;
    private static PreferenceUtil util;

    /**
     * 初始化数据
     *
     * @param context
     */
    private PreferenceUtil(Context context) {
        mPerferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getIntance() {
        synchronized (PreferenceUtil.class) {
            if (util == null) {
                util = new PreferenceUtil(UtilApp.getIntance().getApplicationContext());
            }
        }
        return util;
    }

    /**
     * 根据键值 获取对应的数据 如果没有 就默认值为空
     *
     * @param Key
     * @return
     */
    public String getString(String Key) {
        return mPerferences.getString(Key, "");
    }

    /**
     * 根据键值 获取对应的数据
     *
     * @param Key
     * @param defaultValue
     * @return
     */
    public String getString(String Key, String defaultValue) {
        return mPerferences.getString(Key, defaultValue);
    }


    /**
     * 保存 数据
     *
     * @param Key
     * @param Values
     */
    public void setString(String Key, String Values) {
        SharedPreferences.Editor Editor = mPerferences.edit();
        Editor.putString(Key, Values);
        Editor.commit();
    }

    /**
     * 保存 boolean类型的数据
     *
     * @param Key
     * @param value
     */
    public void setBoolean(String Key, Boolean value) {
        SharedPreferences.Editor Editor = mPerferences.edit();
        Editor.putBoolean(Key, value);
        Editor.commit();
    }

    /**
     * 根据键值返回boolean类型数据 默认值为true
     *
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return mPerferences.getBoolean(key, false);
    }

    /**
     * 根据键值返回boolean类型数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return mPerferences.getBoolean(key, defaultValue);
    }

    /**
     * 获取int 类型数据，默认值为0
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return mPerferences.getInt(key, 0);
    }

    /**
     * 获取int 类型数据，默认值为0
     *
     * @param key
     * @param defaultInt
     * @return
     */
    public int getInt(String key, int defaultInt) {
        return mPerferences.getInt(key, defaultInt);
    }


    public void setInt(String key, int value) {
        SharedPreferences.Editor Editor = mPerferences.edit();
        Editor.putInt(key, value);
        Editor.commit();
    }

    public void setFloat(String Key, float Values) {
        SharedPreferences.Editor Editor = mPerferences.edit();
        Editor.putFloat(Key, Values);
        Editor.commit();
    }

    public float getFloat(String key, float defaultFloat) {
        return mPerferences.getFloat(key, defaultFloat);
    }

    /**
     * 移除数据
     */
    public void removePreference() {
        SharedPreferences.Editor Editor = mPerferences.edit();
        Editor.clear();
        Editor.commit();
    }


    /**
     * desc:保存对象
     *
     * @param key
     * @param obj 要保存的对象，只能保存实现了serializable的对象
     *            modified:
     */
    public void saveObject(String key, Object obj) {
        try {
            // 保存对象
            SharedPreferences.Editor sharedata = mPerferences.edit();
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            sharedata.putString(key, bytesToHexString);
            sharedata.commit();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.loge("保存obj失败----只能保存实现了serializable的对象");
        }
    }

    /**
     * desc:获取保存的Object对象
     *
     * @param key
     * @return modified:
     */
    public Object readObject(String key) {
        try {
            String string = mPerferences.getString(key, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            } else {
                //将16进制的数据转为数组，准备反序列化
                byte[] stringToBytes = StringToBytes(string);
                ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                //返回反序列化得到的对象
                Object readObject = is.readObject();
                return readObject;
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //所有异常返回null
        return null;

    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return modified:
     */
    private String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:将16进制的数据转为数组
     * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
     *
     * @param data
     * @return modified:
     */
    private byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch3;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch3 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch3 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch4;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch4 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch4 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch3 + int_ch4;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }
}
