package com.sxtx.user.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.like.base.base.BaseFragment;
import com.like.base.widget.AutoWidthTabLayout;
import com.lyh.protocol.data.PublicData;
import com.lyh.protocol.login.Api;
import com.sxtx.user.R;
import com.sxtx.user.adapter.VerticalViewPagerAdapter;
import com.sxtx.user.model.bean.LiveBean;
import com.sxtx.user.mvp.presenter.live.RecLiverPresenter;
import com.sxtx.user.mvp.view.live.IRecLiverView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * 創建日期：2019/9/23 on 20:32
 * 介紹:
 * 作者:abc
 */
public class LiveFragment1 extends BaseFragment<RecLiverPresenter> implements IRecLiverView {

    AutoWidthTabLayout tab_home_layout;
    VerticalViewPager viewPager;
    List<PublicData.AnchorData> list = new ArrayList<>();
    List<LiveBean> urlList = new ArrayList<>();

    public static  LiveFragment1 newInstance() {
        LiveFragment1 fragment = new LiveFragment1();
        return fragment;
    }

    @Override
    public Object getResId() {
        return R.layout.fragment_live1;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
//        tab_home_layout = $(R.id.tab_home_layout);
        viewPager = $(R.id.viewPager);
        ImmersionBar.setTitleBar(getActivity(), ImmersionBar.getStatusBarHeight(getActivity()), tab_home_layout);
    }


    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
//        initView();
    }


    private void initView() {
        VerticalViewPagerAdapter pagerAdapter = new VerticalViewPagerAdapter(getChildFragmentManager());
        //设置viewpager 缓存数，可以根据需要调整
        viewPager.setOffscreenPageLimit(10);
        pagerAdapter.setUrlList(urlList);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void getRecommedData(boolean refresh,  Api.GetRecommedThrHostResponse response) {
        if (refresh) {//刷新
            this.list.clear();
            this.list.addAll(response.getDataList());
        } else {//加载
            this.list.addAll(response.getDataList());
        }

        for (int i = 0;i<this.list.size();i++){
            LiveBean bean = new LiveBean();
            bean.address = this.list.get(i).getAddress();
            urlList.add(bean);
        }

        initView();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.getGetRecommedThrHostReqeust(true, "");
    }

    @Override
    public void getLivePlatformData(boolean refresh, @NotNull Api.GetLivePlatformResponse response) {

    }

    @Override
    public void finishRefresh() {

    }

    @Override
    public void noData() {

    }
}
