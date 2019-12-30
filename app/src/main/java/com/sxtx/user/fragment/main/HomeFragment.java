package com.sxtx.user.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.like.base.base.BaseFragment;
import com.like.base.widget.AutoWidthTabLayout;
import com.lyh.protocol.data.PublicData;
import com.sxtx.user.R;
import com.sxtx.user.activity.SearchActivity;
import com.sxtx.user.adapter.ViewpagerFragmentAdapter;
import com.sxtx.user.fragment.home.HomeFilterFragment;
import com.sxtx.user.fragment.home.HomeGcFragment;
import com.sxtx.user.fragment.home.HomeNewFragment;
import com.sxtx.user.fragment.home.HomeRecFragment;
import com.sxtx.user.fragment.home.HomeTopicFragment;
import com.sxtx.user.fragment.update.HomeHintDialog;
import com.sxtx.user.mvp.presenter.main.HomePresenter;
import com.sxtx.user.mvp.view.main.IHomeView;
import com.sxtx.user.util.AppPreference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 創建日期：2019/9/23 on 20:11
 * 介紹:
 * 作者:CHOK
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView, View.OnClickListener {

    AutoWidthTabLayout tab_home_layout;
    ViewPager pager_home;
    private int mCurrentIndex = 2;
    private List<BaseFragment> mFragmentList = null;
    ViewpagerFragmentAdapter fragmentAdapter;
    ImageView img_search;
    LinearLayout toolbar;

    @Override
    public Object getResId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);

        tab_home_layout = $(R.id.tab_home_layout);
        toolbar = $(R.id.toolbar);
        ImmersionBar.setTitleBar(Objects.requireNonNull(getActivity()),toolbar);
        pager_home = $(R.id.pager_home);
        img_search = $(R.id.img_search);
        initListener();
        if (AppPreference.getIntance().getFilter()){
            tab_home_layout.addTab("篩選");//筛选
        }
        tab_home_layout.addTab("最新");
        tab_home_layout.addTab("排行");
        tab_home_layout.addTab("推薦");
        tab_home_layout.addTab("專題");
        tab_home_layout.addTab("國產");
        tab_home_layout.addTab("日韓");
        tab_home_layout.addTab("歐美");

        mFragmentList = new ArrayList<>();
        if (AppPreference.getIntance().getFilter()) {
            mFragmentList.add(HomeFilterFragment.Companion.newIncetance());
        }
        mFragmentList.add(HomeNewFragment.Companion.newIncetance(1));
        mFragmentList.add(HomeNewFragment.Companion.newIncetance(2));
        mFragmentList.add(new HomeRecFragment());
        mFragmentList.add(new HomeTopicFragment());
        mFragmentList.add(HomeGcFragment.Companion.newIncetance(1));
        mFragmentList.add(HomeGcFragment.Companion.newIncetance(2));
        mFragmentList.add(HomeGcFragment.Companion.newIncetance(3));
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

        //这里做配置 是保证进入后默认在推荐页面
        if (AppPreference.getIntance().getFilter()){//有筛选
            mCurrentIndex=3;
        }else {
            mCurrentIndex=2;
        }
        pager_home.setCurrentItem(mCurrentIndex);
        tab_home_layout.getTabLayout().getTabAt(mCurrentIndex).select();
    }

    private void initListener() {
        img_search.setOnClickListener(this);
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        mPresenter.getTrumpetReqeust();
    }


    public void toFragment(int position){
        pager_home.setCurrentItem(position);
        tab_home_layout.getTabLayout().getTabAt(position).select();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    @Override
    public void getAnnouncement(@NotNull PublicData.NoticeData announcement) {
        HomeHintDialog.Companion.newIntance()
                .setTitle(announcement.getTitle())
                .setContent(announcement.getContent())
                .setTopDesc("柚子公告")
                .invoke(new HomeHintDialog.ClickListener() {
                    @Override
                    public void click(@org.jetbrains.annotations.Nullable View v) {

                    }
                })
                .show(getChildFragmentManager(), "show_home_hint");
    }
}
