package com.sxtx.user.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.like.base.base.BaseActivity;
import com.like.base.base.BaseFragment;
import com.sxtx.user.dbdata.AdInfo;
import com.sxtx.user.fragment.launcher.ADFragment;

import java.util.ArrayList;

/**
 * 广告页
 */

public class AdActivity extends BaseActivity {

    ArrayList<AdInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = getIntent().getParcelableArrayListExtra("list");
    }

    @Override
    public BaseFragment setRootFragment() {
        return new ADFragment();
    }

    public ArrayList<AdInfo> getList() {
        return list;
    }
}
