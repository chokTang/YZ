package com.sxtx.user.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.like.utilslib.UtilApp;
import com.like.utilslib.app.AppUtil;
import com.sxtx.user.BuildConfig;
import com.sxtx.user.ConfigData;
import com.sxtx.user.dbdata.CrashData;
import com.sxtx.user.model.encryption.Aes256Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import event.HeartServiceEvents;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 全局异常处理类
 */
public class CrashHandlerUtils implements Thread.UncaughtExceptionHandler {
    public static final String AES_KEY = "9111111111111119";
    public static final String AES_IV = "1234567890123456";
    /**
     * 系统默认UncaughtExceptionHandler
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * context
     */
    private Context mContext;

    /**
     * 存储异常和参数信息
     */
    private Map<String, String> paramsMap = new HashMap<>();

    /**
     * 格式化时间
     */
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");

    private String TAG = this.getClass().getSimpleName();

    private File dir;

    private static CrashHandlerUtils mInstance;

    private OkHttpClient client;

    private CrashHandlerUtils() {

    }

    /**
     * 获取CrashHandler实例
     */
    public static synchronized CrashHandlerUtils getInstance() {
        if (null == mInstance) {
            mInstance = new CrashHandlerUtils();
        }
        return mInstance;
    }

    public void init(Context context) {
        client = new OkHttpClient().newBuilder().build();
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为系统默认的
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    /**
     * uncaughtException 回调函数
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex)&&mDefaultHandler!= null){
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }else{
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }
    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.  可以自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        //获取versionName,versionCode
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                paramsMap.put("versionName", versionName);
                paramsMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info "+e);
        }
        //获取所有系统信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                paramsMap.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info "+ e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();

        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        try {
            String time = format.format(new Date());
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
            String app_versionName = pi.versionName;//app版本名
            String phone_os_version = Build.VERSION.RELEASE+"_"+Build.VERSION.SDK_INT;//Android 版本号
            String phone_model = Build.MODEL;//手机型号
            String fileName = time + "-" + app_versionName
                    +"-"+phone_model+"-"+phone_os_version+".txt";
            String path = "";
            File file = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Crash/";
                dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                file = new File(path,fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                Log.e(TAG, "saveCrashInfo2File: "+sb.toString());
                fos.close();

            }
            CrashData data = new CrashData();
            data.setDeviceId(AppUtil.getIMEI());
            data.setCrashTime(String.valueOf(System.currentTimeMillis()));
            data.setDeviceType("Android");
            data.setOsVer(android.os.Build.VERSION.RELEASE);
            data.setPhoneType(android.os.Build.BRAND+"-"+android.os.Build.MODEL);
            data.setAppId(ConfigData.CHANEL_ID);
            data.setAppVer(AppUtil.getVersionName());
            data.setAppName(AppUtil.getAppName());
            data.setAppPack(UtilApp.getIntance().getApplicationContext().getPackageName());
            data.setCrashType(getCrashType(sb.toString()));
            data.setNotes("");
            //上传崩溃信息
            uploadExceptionToServer(path,file,data);
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file..."+ e);
        }
        //结束程序
        try {
            System.exit(0);
            android.os.Process.killProcess( android.os.Process.myPid());
        } catch (Exception e) {
            System.exit(-1);
        }
        return null;
    }

    /**
     * 获取常见崩溃类型
     */
    private String getCrashType(String sb){
        if (sb.contains("java.lang.IllegalArgumentException")){
            return "IllegalArgument";
        }else if (sb.contains("java.lang.NullPointerException")){
            return "NullPointer";
        }else if (sb.contains("java.util.ConcurrentModificationException")){
            return "ConcurrentModification";
        }else if (sb.contains("java.lang.IllegalStateException")){
            return "IllegalState";
        }else if (sb.contains("Application Not Responding")){
            return "ANR";
        }else if (sb.contains("java.lang.OutOfMemoryError")){
            return "OOM";
        }else if (sb.contains("java.lang.ClassNotFoundException")){
            return "ClassNotFound";
        }else if (sb.contains("java.lang.IndexOutOfBoundsException")){
            return "IndexOutOfBounds";
        }else if (sb.contains("java.lang.IllegalAccessException")){
            return "IllegalAccess";
        }else if (sb.contains("java.lang.ClassCastException")){
            return "ClassCast";
        }else if (sb.contains("NumberFormatException")){
            return "NumberFormatException";
        }else if (sb.contains("NoClassDefFoundError")){
            return "NoClassDefFoundError";
        }else if (sb.contains("java.lang.ArithmeticException")){
            return "ArithmeticException";
        }else if (sb.contains("java.lang.StackOverflowError")){
            return "StackOverflow";
        }else if (sb.contains("android.os.NetworkOnMainThreadException")){
            return "NetworkOnMainThread";
        }else if (sb.contains("java.util.NoSuchElementException")){
            return "NoSuchElementException";
        }else
        return "Exception";
    }


    /**
     * 将异常信息发送到服务器
     */
    private void uploadExceptionToServer(final String path,final File file,final CrashData data) throws IOException {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), Objects.requireNonNull(Bytes2File(Aes256Utils.encrypt(File2byte(file), AES_KEY, AES_IV), path,System.currentTimeMillis()+".txt")));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("crashFile",file.getName(), fileBody)
                .addFormDataPart("parameters",Aes256Utils.encrypt(new Gson().toJson(data),AES_KEY,AES_IV))
                .build();
        String url;
     if (BuildConfig.BASE_URL_RELEASE){
//                  if (true){
            url = AppPreference.getIntance().getOSSUrl()+"/crash_j";
        }else{
            url = "http://192.168.0.160:9090/crash_j";
        }
        //2.构建request
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_UPLOAD_CRASH, request));
    }

    public static byte[] File2byte(File tradeFile){
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer;
    }

    public static File Bytes2File(byte[] bytes, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            // 判断文件目录是否存在
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath,fileName);
            if (!file.exists()){
                file.createNewFile();
            }
            //输出流
            fos = new FileOutputStream(file);

            //缓冲流
            bos = new BufferedOutputStream(fos);

            //将字节数组写出
            bos.write(bytes);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
