package com.sxtx.user.fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;
import com.like.base.base.BaseFragment;
import com.lyh.protocol.login.Api;
import com.sxtx.user.R;
import com.sxtx.user.dialog.VersionDialog;
import com.sxtx.user.dialog.VersionProgressDialog;
import com.sxtx.user.fragment.deposit.DepositType1Fragment;
import com.sxtx.user.fragment.main.DepositFragment;
import com.sxtx.user.fragment.main.HomeFragment;
import com.sxtx.user.fragment.main.LiveFragment;
import com.sxtx.user.fragment.main.LiveFragment1;
import com.sxtx.user.fragment.main.MeFragment;
import com.sxtx.user.fragment.main.TaskFragment;
import com.sxtx.user.mvp.presenter.MainPresenter;
import com.sxtx.user.mvp.view.IMainView;
import com.sxtx.user.util.UpdateManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.ISupportFragment;

/**
 *
 * 介紹:
 */
public class MainFragment extends BaseFragment<MainPresenter> implements IMainView,View.OnClickListener {

    private List<BaseFragment> fragmentList = new ArrayList<>();
    private List<TextView> mTextList = new ArrayList<>();


    DepositFragment fragment = new DepositFragment();
    TextView tv_home_1, tv_home_2, tv_home_4, tv_home_5;
    TextView tv_home_3;
    ImageView img_home_3;
    LinearLayout ll_home_3;

    private int index = 0;//显示哪个fragment

    @Override
    public Object getResId() {
        return R.layout.fragment_main;
    }



    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.GetAppVersion();
     }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {

        tv_home_1 = $(R.id.tv_home_1);
        tv_home_2 = $(R.id.tv_home_2);
        tv_home_3 = $(R.id.tv_home_3);
        ll_home_3 = $(R.id.ll_home_3);
        img_home_3 = $(R.id.img_home_3);
        tv_home_4 = $(R.id.tv_home_4);
        tv_home_5 = $(R.id.tv_home_5);

        tv_home_1.setOnClickListener(this);
        tv_home_2.setOnClickListener(this);
        ll_home_3.setOnClickListener(this);
        tv_home_4.setOnClickListener(this);
        tv_home_5.setOnClickListener(this);

        img_home_3.setBackgroundResource(R.drawable.icon_home_3);
        //startScaleAnim(img_home_3,true,1000);
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {


        mTextList.add(tv_home_1);
        mTextList.add(tv_home_2);
        mTextList.add(tv_home_3);
        mTextList.add(tv_home_4);
        mTextList.add(tv_home_5);

        fragmentList.add(new HomeFragment());
        fragmentList.add(new LiveFragment());
        fragmentList.add(fragment);
        fragmentList.add(TaskFragment.newInstance(false));
        fragmentList.add(new MeFragment());

        loadMultipleRootFragment(R.id.framelayout, index, fragmentList.toArray(new ISupportFragment[5]));
        clickItem(index);
        initDownDialog();
    }

    private void initDownDialog() {
        versionProgressDialog = new VersionProgressDialog(getActivity());
        versionProgressDialog.setOnClickListener(new VersionProgressDialog.OnClickListener() {
            @Override
            public void onClose() {
                }

            @Override
            public void onRetry() {

                versionProgressDialog.reset();
                mPresenter.download(androinAddress);
            }
        });
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        if (versionDialog != null) {
            versionDialog.dismiss();
            versionDialog = null;
        }

        if (versionProgressDialog != null) {
            versionProgressDialog.dismiss();
            versionProgressDialog = null;
        }



    }




    /**
     * 点击了哪个
     *
     * @param postion
     */
    private void clickItem(int postion) {
        showHideFragment(fragmentList.get(postion), fragmentList.get(index));
        for (int i = 0; i < 5; i++) {
            if (i == postion) {
                mTextList.get(i).setSelected(true);
                mTextList.get(i).setTextColor(getResources().getColor(R.color.color_EF373A));
            } else {
                mTextList.get(i).setSelected(false);
                mTextList.get(i).setTextColor(getResources().getColor(R.color.color_434343));
            }
        }
        index =postion;
    }

    @Override
    public void onClick(View view) {
        startScaleAnim(view,false,400);
        switch (view.getId()) {
            case R.id.tv_home_1:
                fragment.isRequest = false;
                clickItem(0);
                break;
            case R.id.tv_home_2:
                fragment.isRequest = false;
                clickItem(1);
//                start(LiveFragment1.newInstance());
                break;
            case R.id.ll_home_3:
//                fragment.isRequest = true;
//                clickItem(2);
                mPresenter.getPayDataResponse();
                break;
            case R.id.tv_home_4:
                fragment.isRequest = false;
                clickItem(3);
                break;
            case R.id.tv_home_5:
                fragment.isRequest = false;
                clickItem(4);
                break;
        }
    }

    /**
     * 缩放动画
     * @param view
     * @param isRepeat
     * @param duration
     */
    private void startScaleAnim(View view,boolean isRepeat,int duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view,"scaleX",1f,0.8f,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view,"scaleY",1f,0.8f,1f);
        if (isRepeat){
            scaleX.setRepeatCount(ValueAnimator.INFINITE);
            scaleY.setRepeatCount(ValueAnimator.INFINITE);
        }
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY);
        set.setDuration(duration);
        set.start();
    }

    @Override
    public void getPayData(@NotNull Api.GetPayDataResponse respons) {
        switch (respons.getTopYpType()) {//1-选普通 2-卡密
            case 1:
                start(DepositType1Fragment.Companion.newIncetance(1));
                break;
            case 2:
                start(DepositType1Fragment.Companion.newIncetance(2));
                break;
        }
    }

    @Override
    public void getAppVersionSucceed(@NotNull String androinVersion, @NotNull String androinAddress,String content,String title) {
         if (  UpdateManager.getInstance().isNeedUpdate(androinVersion)){
             showVersionDialog(androinAddress,content,title);
         }

    }
    private VersionDialog versionDialog = null;
    public void showVersionDialog(final String androinAddress,String content,String title){
        try {
            versionDialog = new VersionDialog(getContext(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainFragment.this.download(androinAddress);
                }
            })
                    .setContent(content,title) ;
            if (!getActivity().isFinishing() && !versionDialog.isShowing()) {
                versionDialog.show();
            }

        }catch (Exception e){
            LogUtil.e(TAG,e.toString());
        }
    }

    private VersionProgressDialog versionProgressDialog;
    private String androinAddress="";

    private void download(String androinAddress) {
        if (!mPresenter.checkPermission(getActivity())) {
            mPresenter.requestPermission(getActivity());
            return;
        }

        if (versionDialog != null) {
            versionDialog.dismiss();
            versionDialog = null;
        }
       this. androinAddress=androinAddress;
        mPresenter.download(androinAddress);
    }


    @Override
    public void showDownloadProgressDialog() {
        if (versionProgressDialog.isShowing()) {
            versionProgressDialog.reset();
        } else {
            versionProgressDialog.show();
        }
    }

    @Override
    public void updateDownloadProgress(int progress, int total) {
        versionProgressDialog.setProgress((int) Math.floor(progress * 100.0f / total));
    }

    @Override
    public void showDownloadErrorDialog() {
        versionProgressDialog.downloadFail();
    }
}
