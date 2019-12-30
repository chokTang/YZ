package com.sxtx.user.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sxtx.user.R;
import com.sxtx.user.dialog.base.PBaseDialog;

public class VersionProgressDialog extends PBaseDialog {


    private TextView tvProgress;
    private ProgressBar progressBar;
    private TextView tvTitle;
    private ImageView ivIcon;
    private Button btnRetry;
    Context mcontext;

    private OnClickListener onClickListener;

    public VersionProgressDialog(Context context) {
        super(context);
        mcontext=context;
        canceledOnTouchOutside(false);
        setCancelable(false);
        contentView(R.layout.dialog_version_progress);


        tvTitle = findViewById(R.id.tv_title);
        tvProgress = findViewById(R.id.tv_progress);
        progressBar = findViewById(R.id.progress_bar);
        ivIcon = findViewById(R.id.progress_icon);
        btnRetry = findViewById(R.id.btn_retry);
        ImageButton btnClose = findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClose();
                }
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onRetry();
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public VersionProgressDialog setProgress(int progress) {
        progressBar.setProgress(progress);
        tvProgress.setText(progress + "%");

        int tw = progressBar.getMeasuredWidth() - dip2px(18);
        float w = progress / 100f * tw;
        ivIcon.setTranslationX(w);
        return this;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public  int dip2px(float dpValue) {
        final float scale = mcontext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void reset() {
        tvTitle.setText("更新中…請不要關閉頁面");
        tvProgress.setText("0%");
        tvProgress.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        ivIcon.setTranslationX(0f);
        ivIcon.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(View.GONE);


    }

    public VersionProgressDialog downloadFail() {
        ivIcon.setVisibility(View.INVISIBLE);
        tvTitle.setText("更新失敗");
        tvProgress.setVisibility(View.INVISIBLE);
        progressBar.setProgress(0);
        btnRetry.setVisibility(View.VISIBLE);


        return this;
    }

    public interface OnClickListener {
        void onClose();
        void onRetry();
    }
}
