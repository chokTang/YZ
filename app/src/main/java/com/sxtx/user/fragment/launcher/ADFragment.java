package com.sxtx.user.fragment.launcher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.like.base.base.BaseFragment;
import com.like.utilslib.UtilApp;
import com.sxtx.user.R;
import com.sxtx.user.activity.AdActivity;
import com.sxtx.user.activity.HomeActivity;
import com.sxtx.user.dbdata.AdInfo;
import com.sxtx.user.mvp.presenter.account.AdPresenter;
import com.sxtx.user.mvp.view.main.IAdView;
import com.sxtx.user.service.HeartService;
import com.sxtx.user.util.GlideOptionUtil;
import com.sxtx.user.util.UrlCheckUtils;
import com.sxtx.user.util.timer.BaseTimerTask;
import com.sxtx.user.util.timer.ITimerListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Timer;

public class ADFragment extends BaseFragment<AdPresenter> implements IAdView, ITimerListener, View.OnClickListener {
    ViewPager viewPager;
    private AppCompatTextView launcher_timer_tv;
    private LinearLayout ll_jump;
    private Timer mTimer = null;
    private int mCount = 5;
    private boolean isClick = false;
    private String mUrl = "";
    ArrayList<AdInfo> mData = new ArrayList<>();
    @Override
    public Object getResId() {
        return R.layout.fragment_ad;
    }


    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        viewPager = (ViewPager)$(R.id.viewPager);
        launcher_timer_tv = (AppCompatTextView) $(R.id.launcher_timer_tv);
        ll_jump = (LinearLayout) $(R.id.ll_jump);
        ll_jump.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mData = ((AdActivity)activity).getList();
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mData.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            //初始化item布局
            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                ImageView imageView = new ImageView(getContext());
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(getActivity()).load(mData.get(position).getImg()).apply(GlideOptionUtil.Companion.getAdOption()).into(imageView);
                container.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTimer!=null)
                        mTimer.cancel();
                        mTimer = null;
                        mUrl = mData.get(position).getUrl();
                        isClick = true;
                        if (UrlCheckUtils.checkUrlIsEffective(mUrl)){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(mUrl))));
                            mPresenter.clickAd(mData.get(position).getId());
                        }else
                            showToast("鏈接無效");
                    }
                });
                return imageView;
            }

            //销毁item
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        BaseTimerTask task = new BaseTimerTask(this);
        mTimer = new Timer();
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ll_jump) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
                toHome();
            }
        }
    }

    @Override
    public void onTimer() {
        getBaseActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (launcher_timer_tv != null) {
                    if (mCount <= 0){
                        launcher_timer_tv.setVisibility(View.GONE);
                    }else{
                        launcher_timer_tv.setVisibility(View.VISIBLE);
                    }
                    launcher_timer_tv.setText(MessageFormat.format("{0}s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                            toHome();
                        }
                    }
                }
            }
        });
    }

    private void toHome() {
        Intent startIntent = new Intent(UtilApp.getIntance().getApplicationContext(), HeartService.class);
        UtilApp.getIntance().getApplicationContext().startService(startIntent);
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (isClick)
            toHome();
    }
}
