package com.like.base.net.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.like.base.R;
import com.like.utilslib.screen.DensityUtil;

/**
 * 网络请求等待框
 * Created by longshao on 2017/3/20.
 */

public class HttpResultDialog extends Dialog {

    private ImageView dialog_iv;
    private AnimationDrawable animationDrawable;
    private Context context;
    private LinearLayout request_layout;
    private TextView dialog_tv;

    public HttpResultDialog(Context context) {
        super(context, R.style.waitDialog);
        setCanceledOnTouchOutside(false);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);//设置返回键不可用
        initView();
        initData();
    }

    private void initData() {
        dialog_iv.setBackgroundResource(R.drawable.imgloading);
        animationDrawable = (AnimationDrawable) dialog_iv.getBackground();
        animationDrawable.start();
    }

    private void initView() {
        setContentView(R.layout.dialog_httpresult_layout);
        dialog_iv = (ImageView) findViewById(R.id.dialog_iv);
        dialog_tv = (TextView) findViewById(R.id.dialog_tv);
        dialog_tv = (TextView) findViewById(R.id.dialog_tv);
        request_layout = (LinearLayout) findViewById(R.id.request_layout);

        final int width = DensityUtil.dpTopx(140);
        final int height = DensityUtil.dpTopx(140);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        request_layout.setLayoutParams(params);
    }

    /**
     * 设置内容
     * @param value
     */
    public void setTextContext(String value){
        dialog_tv.setText(value);
    }

    @Override
    public void cancel() {
        super.cancel();
    }
}
