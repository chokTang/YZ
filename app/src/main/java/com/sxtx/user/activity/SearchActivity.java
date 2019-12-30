package com.sxtx.user.activity;

import com.like.base.base.BaseActivity;
import com.like.base.base.BaseFragment;
import com.sxtx.user.fragment.main.SearchFragment;

/**
 * 搜索播放
 */

public class SearchActivity extends BaseActivity {

    @Override
    public BaseFragment setRootFragment() {
        return new SearchFragment();
    }
}
