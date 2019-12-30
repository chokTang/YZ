package com.sxtx.user;

/**
 * 介紹:
 * 作者:CHOK
 */
public class ConfigData {
    //true 代表02服务器  false 代表61服务器
    public static final boolean DEBUG = false;
    public static final String SERVER_02 = "http://192.168.0.02:8089";
    //    public static final String SERVER_02 = "https://llgoin-v.fun33.app:443";
    public static final String SERVER_61 = "http://192.168.0.61:8089";
    //    public static final String SERVER_61 = "http://192.168.0.103:8089";
    public static final String BASE_URL = DEBUG ? SERVER_02 : SERVER_61;

    public static final String CHANEL_ID = "dabaitu";
    public static final String AES_KEY = "2dsfedwd85erf535";
    public static final String MEDIA_TYPE = "application/octet-stream";
    public static final String AES_IV = "ubateubatequanli";
    public static final String REQUEST_LOGIN_URL = "http://192.168.0.61/version/version.txt";
    public static final int DECODE_IMG_KEY = 10844680;
    //賬號登錄
    public static String OTHER_PHONE = "";
    public static String OTHER_PASSW = "";


    /**
     * 阿里云相关
     */
    // 访问的endpoint地址
    public static final String OSS_ENDPOINT = "http://oss-cn-hongkong.aliyuncs.com";
    public static final String OSS_OBJECT_NAME = "Applications-dbt/dabaitu-apps_con/version.txt";
    public static final String BUCKET_NAME = "dabt";
    public static final String OSS_ACCESS_KEY_ID = "LTAI4FuqXYFDkhBYc9Wdkohd";
    public static final String OSS_ACCESS_KEY_SECRET = "2MKILthDIpiSXPG0YZ2sLDrISxWrv4";
}
