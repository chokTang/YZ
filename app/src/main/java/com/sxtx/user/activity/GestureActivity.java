package com.sxtx.user.activity;

import android.view.KeyEvent;

import com.like.base.base.BaseActivity;
import com.like.base.base.BaseFragment;
import com.sxtx.use.GestureFragment;

public class GestureActivity extends BaseActivity {

    @Override
    public BaseFragment setRootFragment() {
        return new GestureFragment().newInstance(true,false);
    }

   /*
   * 屏蔽返回鍵*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

         if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

