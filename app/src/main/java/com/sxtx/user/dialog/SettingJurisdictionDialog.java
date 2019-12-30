package com.sxtx.user.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sxtx.user.R;
import com.sxtx.user.dialog.base.BaseDialog;

public class SettingJurisdictionDialog extends BaseDialog {

    OnCallBackListener onCallBackListener;
    Button btn_cancle,btn_sure;
    TextView tvContent;

    public SettingJurisdictionDialog setOnCallBackListener(OnCallBackListener onCallBackListener) {
        this.onCallBackListener = onCallBackListener;
        return this;
    }






    private String content;

    public SettingJurisdictionDialog setContent(String content) {
        this.content = content;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        View dialogView = inflater.inflate(R.layout.dialog_setting_jurisdiction, container, false);
        initView(dialogView);
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
        initData();
        initListener();
        return dialogView;
    }

    private void initView(View view) {
        tvContent=view.findViewById(R.id.tv_content);
        btn_cancle=view.findViewById(R.id.btn_cancle);
        btn_sure=view.findViewById(R.id.btn_sure);
    }

    private void initData() {


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initListener() {
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onCallBackListener != null) onCallBackListener.onCallBack(1);
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onCallBackListener != null) onCallBackListener.onCallBack(2);
            }
        });
    }


    public interface OnCallBackListener {
        void onCallBack(int swich);
    }
}
