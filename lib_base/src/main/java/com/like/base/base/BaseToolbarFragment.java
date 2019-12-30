package com.like.base.base;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.like.base.R;
import com.like.base.base.presenter.BasePresenter;

/**
 * 基础有toolbar页面
 * Created by longshao on 2017/10/13.
 */

public abstract class BaseToolbarFragment<P extends BasePresenter> extends BaseFragment<P> {

    protected Toolbar mToolbar;
    protected TextView mToolbarTitle;
    protected TextView mToolbarSubTitle;
    protected TextView mToolbarRight;

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        mToolbar = $(R.id.toolbar);
        mToolbarTitle = $(R.id.toolbar_title);
        mToolbarSubTitle = $(R.id.toolbar_subtitle);
        mToolbarRight = $(R.id.tv_right);
    }

    protected void setToolbarTitle(String title) {
        mToolbarTitle.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(title);
    }

    protected void setToolbarSubTitle(String subTitle) {
        mToolbarSubTitle.setVisibility(View.VISIBLE);
        mToolbarSubTitle.setText(subTitle);
    }

    protected void onBack() {
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });
    }

    protected void setRightText(String value, View.OnClickListener clickListener) {
        mToolbarRight.setText(value);
        mToolbarRight.setVisibility(View.VISIBLE);
        if (clickListener != null) {
            mToolbarRight.setOnClickListener(clickListener);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(mToolbar).init();
    }

    protected void setRightIcon(@MenuRes Integer des, Toolbar.OnMenuItemClickListener listener) {
        mToolbar.inflateMenu(des);
        if (listener != null) {
            mToolbar.setOnMenuItemClickListener(listener);
        }
    }
}
