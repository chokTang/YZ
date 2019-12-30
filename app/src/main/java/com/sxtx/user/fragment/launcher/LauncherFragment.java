package com.sxtx.user.fragment.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;
import com.like.base.base.BaseFragment;
import com.like.base.base.inter.IPermissonResultListener;
import com.lyh.protocol.data.PublicData;
import com.sxtx.user.BuildConfig;
import com.sxtx.user.CdLongShaoAppaction;
import com.sxtx.user.R;
import com.sxtx.user.activity.AdActivity;
import com.sxtx.user.dbdata.AdInfo;
import com.sxtx.user.mvp.presenter.account.LoginPresenter;
import com.sxtx.user.mvp.view.account.ILoginView;
import com.sxtx.user.util.CheckNetworkProxyUtil;
import com.sxtx.user.util.CheckPhoneUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * APP的启动页面
 * Created by Administrator on 2017/7/31.
 */

public class LauncherFragment extends BaseFragment<LoginPresenter> implements ILoginView {
    private String sid;
    @Override
    public Object getResId() {
        return R.layout.fragment_launcher_layout;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        if (CheckPhoneUtil.Companion.checkIsNotRealPhone()){
            if (BuildConfig.BASE_URL_RELEASE){
               System.exit(0);
            }else{
                showToast("請使用真機體驗");
            }
        }
    }


    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        if (CheckNetworkProxyUtil.isWifiProxy(_mActivity))//监听到网络代理退出程序
            System.exit(0);
        else{
            String[] permission = {"android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.CAMERA",
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_PHONE_STATE"};
            onRequestPermisson(permission, new IPermissonResultListener() {
                @Override
                public void onSuccess() {
                    login();

                }

                @Override
                public void onFail(List<String> fail) {
                    showToast("您當前未給手機權限");
                }
            });
        }
    }

    private void login() {
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
                Log.d("OpenInstall", "getInstall : installData = " + appData.toString());
                if (TextUtils.isEmpty(bindData)){
                    sid = "";
                }else {
                    try {
                        JSONObject obj = new JSONObject(bindData);
                        sid = obj.optString("ssid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sid = "";
                    }
                }
                mPresenter.getLoginUrl(true,sid);
            }
        });
    }


    @Override
    public void sendHeart() {
        mPresenter.onLoadingImage();
    }

    @Override
    public void getDataSucc(final List<PublicData.AdvertisingData> list) {
        final ArrayList<AdInfo> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AdInfo in = new AdInfo();
            in.setImg(list.get(i).getPictureAddress());
            in.setUrl(list.get(i).getExternalAddress());
            in.setId(list.get(i).getId());
            data.add(in);
        }
        Intent in = new Intent(getActivity(), AdActivity.class);
        in.putParcelableArrayListExtra("list",data);
        startActivity(in);
        getActivity().finish();
    }

    @Override
    public void loginSucc() {

    }


    @Override
    public void onDestroy() {
        mPresenter.stopTask();
        super.onDestroy();
    }
}
