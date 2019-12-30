package com.sxtx.user.inter;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.sxtx.user.ConfigData;

/**
 * OSS初始化
 */
public class OSSClientBuilder {
    public static OSSClient Builder(Context context){
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ConfigData.OSS_ACCESS_KEY_ID, ConfigData.OSS_ACCESS_KEY_SECRET, "");
        // 创建OSSClient实例。
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        return new OSSClient(context, ConfigData.OSS_ENDPOINT, credentialProvider, conf);
    }

    public static GetObjectRequest getObjectRequest(){
        return new GetObjectRequest(ConfigData.BUCKET_NAME, ConfigData.OSS_OBJECT_NAME);
    }
}
