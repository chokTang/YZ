package com.sxtx.user.fragment.guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.like.base.base.BaseFragment;
import com.sxtx.user.R;
import com.sxtx.user.inter.ILoginState;
import com.sxtx.user.util.AppPreference;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by Administrator on 2017/7/31.
 */

public class GuideFragment extends BaseFragment {
    private BGABanner guide_banner;
    private ILoginState mILoginState;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILoginState) {
            mILoginState = (ILoginState) activity;
        }
    }

    @Override
    public Object getResId() {
        return R.layout.fragment_guide_layout;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        guide_banner = (BGABanner) $(R.id.guide_banner);
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        guide_banner.setEnterSkipViewIdAndDelegate(R.id.guide_signin_tv, 0, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                /*设置已经进入过引导页*/
                AppPreference.getIntance().setFirstLogin(false);
                if (mILoginState != null) {
                    mILoginState.enterLoginPage();
                }
            }
        });
        guide_banner.setData(R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3);
    }
}
