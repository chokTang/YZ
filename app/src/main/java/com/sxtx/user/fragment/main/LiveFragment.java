package com.sxtx.user.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.gyf.immersionbar.ImmersionBar;
import com.like.base.base.BaseFragment;
import com.like.base.widget.AutoWidthTabLayout;
import com.sxtx.user.R;
import com.sxtx.user.adapter.ViewpagerFragmentAdapter;
import com.sxtx.user.fragment.live.RecLiverFragment;
import com.sxtx.user.mvp.presenter.main.LivePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 創建日期：2019/9/23 on 20:32
 * 介紹:
 * 作者:abc
 */
public class LiveFragment extends BaseFragment<LivePresenter> {

    AutoWidthTabLayout tab_home_layout;
    ViewPager pager_home;
    private int mCurrentIndex = 0;
    private List<BaseFragment> mFragmentList = null;
    ViewpagerFragmentAdapter fragmentAdapter;

    @Override
    public Object getResId() {
        return R.layout.fragment_live;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        tab_home_layout = $(R.id.tab_home_layout);
        pager_home = $(R.id.pager_home);
        ImmersionBar.setTitleBar(getActivity(),ImmersionBar.getStatusBarHeight(getActivity()),tab_home_layout);
    }


    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        tab_home_layout.addTab("推薦主播");
        tab_home_layout.addTab("直播平臺");

        mFragmentList = new ArrayList<>();
        mFragmentList.add(RecLiverFragment.Companion.newIncetance(1));
        mFragmentList.add(RecLiverFragment.Companion.newIncetance(2));
        fragmentAdapter = new ViewpagerFragmentAdapter(getChildFragmentManager(), mFragmentList);

        pager_home.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                tab_home_layout.getTabLayout().getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //设置关联和加载fragment
        pager_home.setAdapter(fragmentAdapter);
        tab_home_layout.setupWithViewPager(pager_home);

        pager_home.setCurrentItem(mCurrentIndex);
        tab_home_layout.getTabLayout().getTabAt(mCurrentIndex).select();
    }
}
