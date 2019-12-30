package com.sxtx.user.fragment.main;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.like.base.base.BaseFragment;
import com.like.base.base.BaseStatusFragment;
import com.like.base.widget.AutoWidthTabLayout;
import com.lyh.protocol.data.PublicData;
import com.lyh.protocol.login.Api;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.sxtx.user.R;
import com.sxtx.user.adapter.ViewpagerFragmentAdapter;
import com.sxtx.user.fragment.task.InviteFragment;
import com.sxtx.user.fragment.task.TaskFragment1;
import com.sxtx.user.fragment.task.TaskFragment2;
import com.sxtx.user.mvp.presenter.main.TaskPresenter;
import com.sxtx.user.mvp.view.main.ITaskView;
import com.sxtx.user.util.QrCodeUtil;
import com.sxtx.user.widget.NoScrollViewpager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 創建日期：2019/9/23 on 20:38
 * 介紹:
 * 作者:CHOK
 */
public class TaskFragment extends BaseStatusFragment<TaskPresenter> implements ITaskView, View.OnClickListener {


    BaseFragment parentFragment;
    private int mCurrentIndex = 0;
    TextView tv_copy_code, tv_copy_url,tv_code,tv_url;
    RelativeLayout rl_save_ewm;
    LinearLayout mTitleLayout;
    ImageView img_ewm,img_invite,img_back,dialog_iv;
    AutoWidthTabLayout tab_task_layout;
    NoScrollViewpager pager_task;
    private List<BaseFragment> mFragmentList = null;
    ViewpagerFragmentAdapter  fragmentAdapter;
    private Bitmap qrCodeBitmap = null;
    boolean isFirst  = false;
    boolean isMe  = true;
    private AnimationDrawable animationDrawable;

    @Override
    public Integer getMainResId() {
        return R.layout.fragment_task;
    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        mPresenter.getQrCode();
    }

    public static TaskFragment newInstance(boolean isMe) {

        Bundle args = new Bundle();
        args.putBoolean("isMe",isMe);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        showView(MAIN_VIEW);
        Bundle bundle = getArguments();
        isMe = bundle.getBoolean("isMe",true);

        tv_copy_code = $(R.id.tv_copy_code);
        tv_code = $(R.id.tv_code);
        tv_url = $(R.id.tv_url);
        mTitleLayout = $(R.id.title_layout);
        tv_copy_url = $(R.id.tv_copy_url);
        rl_save_ewm = $(R.id.rl_save_ewm);
        img_ewm = $(R.id.img_ewm);
        img_invite = $(R.id.img_invite);
        tab_task_layout = $(R.id.tab_task_layout);
        pager_task = $(R.id.pager_task);
        img_back = $(R.id.img_back);
        dialog_iv = $(R.id.dialog_iv);
        dialog_iv.setBackgroundResource(R.drawable.imgloading);
        animationDrawable = (AnimationDrawable) dialog_iv.getBackground();
        dialog_iv.setVisibility(View.VISIBLE);
        animationDrawable.start();
        if (isMe){
            img_back.setVisibility(View.VISIBLE);
        }else{
            img_back.setVisibility(View.GONE);
        }
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        ImmersionBar.setTitleBar(Objects.requireNonNull(getActivity()), mTitleLayout);
        //当前请求一次数据是 为了首次进入fragment加载UI快
        mPresenter.getQrCode();
        isFirst = true;

    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        setListener();
        tab_task_layout.addTab("邀請任務");
        tab_task_layout.addTab("普通任務");

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new TaskFragment1());
        mFragmentList.add(new TaskFragment2());
        fragmentAdapter = new ViewpagerFragmentAdapter(getChildFragmentManager(), mFragmentList);

        pager_task.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                tab_task_layout.getTabLayout().getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置关联和加载fragment
        pager_task.setAdapter(fragmentAdapter);
        tab_task_layout.setupWithViewPager(pager_task);

        pager_task.setCurrentItem(mCurrentIndex);
        tab_task_layout.getTabLayout().getTabAt(mCurrentIndex).select();

    }

    public void setListener() {
        tv_copy_code.setOnClickListener(this);
        tv_copy_url.setOnClickListener(this);
        img_invite.setOnClickListener(this);
        img_back.setOnClickListener(this);
        rl_save_ewm.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //保存二維碼
                if (qrCodeBitmap != null){
                    QrCodeUtil.saveImage(_mActivity,qrCodeBitmap);
                    mPresenter.saveQrCode();
                }else{
                    showToast("保存失敗");
                }
                return false;
            }
        });
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //防止第一次進fragment加載兩次數據
        if (!isFirst){
            mPresenter.getQrCode();
        }
        isFirst = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_invite:
                parentFragment = (BaseFragment) getParentFragment();
                if (isMe)
                    start(new InviteFragment());
                else{
                    if (parentFragment!=null)parentFragment.start(new InviteFragment());
                }
                break;
            case R.id.tv_copy_code:
                QrCodeUtil.copy(getActivity(),tv_code.getText().toString(),"複製邀請碼成功");
                break;

            case R.id.tv_copy_url:
                QrCodeUtil.copy(getActivity(),tv_url.getText().toString(),"複製鏈接成功");
                break;
            case R.id.img_back:
                pop();
                break;
        }
    }

    @Override
    public void getTaskSucc(List<PublicData.InviteData> list) {

    }

    @Override
    public void getTaskCommonSucc(List<PublicData.CommomData> list) {

    }




    @SuppressLint("CheckResult")
    @Override
    public void getQrCodeSucc(Api.GetQrCodeResponse response) {
        tv_code.setText(response.getQrCode().getInviteCode());
        tv_url.setText(response.getQrCode().getGeneralizeUrl());
        qrCodeBitmap = QrCodeUtil.createQRCodeBitmap(response.getQrCode().getGeneralizeUrl(), DensityUtil.dp2px(105),DensityUtil.dp2px(105));
        img_ewm.setImageBitmap(qrCodeBitmap);
        animationDrawable.stop();
        dialog_iv.setVisibility(View.GONE);
    }
}
