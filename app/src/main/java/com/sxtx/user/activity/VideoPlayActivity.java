package com.sxtx.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.like.base.base.BaseActivity;
import com.like.base.base.BaseFragment;
import com.sxtx.user.fragment.home.VideoPlayFragment;

/**
 * 禁止旋转后其他页面跟着旋转
 */

public class VideoPlayActivity extends BaseActivity {

    public Long videoId;
    public static final String KEY_ACTIVITY_VIDEO_ID = "keyvideo_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        videoId=intent.getLongExtra(KEY_ACTIVITY_VIDEO_ID,0L);
        super.onCreate(savedInstanceState);
    }

    @Override
    public BaseFragment setRootFragment() {
        return VideoPlayFragment.Companion.newIncetance(videoId);
    }

}
