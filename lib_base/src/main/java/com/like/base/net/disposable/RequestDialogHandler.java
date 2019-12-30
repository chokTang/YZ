package com.like.base.net.disposable;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.like.base.net.inter.RequestCancelListener;
import com.like.base.net.widget.HttpResultDialog;

/**
 * 实现对话框消失之后，取消http请求的消息
 * Created by longshao on 2016/6/20 0020.
 */

public class RequestDialogHandler extends Handler {
    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private HttpResultDialog dialog;

    private Context context;
    private RequestCancelListener progressCancelListener;

    public RequestDialogHandler(Context context, RequestCancelListener progressCancelListener) {
        super();
        this.context = context;
        this.progressCancelListener = progressCancelListener;
    }

    /**
     * 显示对话框
     */
    private void showProgressDialog() {
        if (dialog == null) {
            dialog = new HttpResultDialog(context);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    progressCancelListener.onCancleRequest();
                }
            });
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    /**
     * 关闭对话框
     */
    private void dismissProgressDialog() {
        if (context != null) {
            Activity activity = (Activity) context;
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                showProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
