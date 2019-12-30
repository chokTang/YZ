package com.like.base.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.like.base.R;
import com.like.base.base.presenter.BasePresenter;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/4/23.
 */

public abstract class BaseStatusToolbarFragment<P extends BasePresenter> extends BaseStatusFragment<P> {

    protected Toolbar mToolbar;
    protected TextView mToolbarTitle;
    protected TextView mToolbarSubTitle;
    protected TextView mToolbarRightTitle;
    protected LinearLayout lineTitle;


    protected final MyHandler mHandler = new MyHandler(this);


    public static class MyHandler extends Handler {
        private final WeakReference<Fragment> mFragment;

        private MyHandler(Fragment fragment) {
            mFragment = new WeakReference<>(fragment);
        }


        @Override
        public void handleMessage(Message msg) {
            Fragment fragment = mFragment.get();
            switch (msg.what) {


            }
        }

    }


    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        mToolbar = $(R.id.toolbar);
        mToolbarTitle = $(R.id.toolbar_title);
        mToolbarSubTitle = $(R.id.toolbar_subtitle);
        mToolbarRightTitle = $(R.id.tv_right);
        lineTitle = $(R.id.line_toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(mToolbar).init();
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        lineTitle.setVisibility(View.GONE);
    }

    protected void setToolbarTitle(String title) {
        mToolbarTitle.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(title);
    }

    protected void setToolbarSubTitle(String subTitle) {
        mToolbarSubTitle.setVisibility(View.VISIBLE);
        mToolbarSubTitle.setText(subTitle);
    }

    protected void setToolbarRightTitle(String rightTitle) {
        mToolbarRightTitle.setVisibility(View.VISIBLE);
        mToolbarRightTitle.setText(rightTitle);
    }

    protected void setShowLineTitle(Boolean isShow) {
        if (isShow) {
            lineTitle.setVisibility(View.VISIBLE);
        } else {
            lineTitle.setVisibility(View.GONE);
        }
    }


    protected TextView getRightTitle() {
        return mToolbarRightTitle;
    }

    protected void onBack() {
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NoFastClickUtils.isFastClick()) {
                    pop();
                }
            }
        });
    }

    protected void onBack(int drawble) {

        mToolbar.setNavigationIcon(drawble);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NoFastClickUtils.isFastClick()) {
                    pop();
                }
            }
        });
    }

    @Override
    public Object getResId() {
        return R.layout.fragment_base_status_toolbar_layout;
    }
}
