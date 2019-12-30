package com.sxtx.user.fragment.update;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.like.base.dialog.BaseDialogFragment;
import com.like.utilslib.screen.ScreenUtil;
import com.sxtx.user.R;


/**
 * 更新提示
 * Created by Administrator on 2018/3/13.
 */

public class UpdateHintDialog extends BaseDialogFragment {

    private TextView tvContent;
    private TextView tvUpdate;
    private ImageView ivCancle;
    private ProgressBar progressBar;

    private String contentStr;

    @Override
    public Object getResId() {
        return R.layout.dialog_update;
    }

    @Override
    public void initView() {
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvUpdate = (TextView) findViewById(R.id.tv_update);
        ivCancle = (ImageView) findViewById(R.id.iv_cancle);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    public void initData() {
        setCancelable(false);
        if (!TextUtils.isEmpty(contentStr)) {
            tvContent.setText(contentStr);
        }

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvUpdate.setVisibility(View.GONE);
                ivCancle.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);


                if (onUpdateListener != null) {
                    onUpdateListener.onUpdate();
                }
            }
        });
        ivCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDailogDismiss();
                if (onUpdateListener != null) {
                    onUpdateListener.onCancle();
                }
            }
        });
    }

    @Override
    public int getViewWidth() {
        return (int) (ScreenUtil.getScreenWidth()*0.8);
    }

    @Override
    public int getViewHeight() {
        return -2;
    }

    @Override
    public int getViewGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getAnimationType() {
        return CNTER;
    }

    public void setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            contentStr = content;
        }
    }

    public void setMax(int max) {
        if (progressBar != null) {
            progressBar.setMax(max);
        }
    }

    public void setCurrentProgress(int current) {
        if (progressBar != null) {
            progressBar.setProgress(current);
        }

    }

    private OnUpdateListener onUpdateListener;

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        if (onUpdateListener != null) {
            this.onUpdateListener = onUpdateListener;
        }
    }

    public interface OnUpdateListener {
        void onUpdate();

        void onCancle();
    }
}

