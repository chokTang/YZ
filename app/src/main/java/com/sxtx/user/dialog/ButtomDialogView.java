package com.sxtx.user.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sxtx.user.R;


/**
 * 自定义底部弹出对话框
 * Created by on 2019/5/7.
 */
public class ButtomDialogView extends Dialog {

    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private Context context;
    public TextView tv_open_qr, tv_open_pic;
    private TwoSelDialog mTwoSelDialog;
    private static ButtomDialogView dialog;

    public ButtomDialogView(@NonNull Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
        init();
    }

    public static ButtomDialogView newInstance(Context context) {
        dialog = new ButtomDialogView(context);
        return dialog;
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_open_buttom, null);
        setContentView(view);

        tv_open_pic = (TextView) findViewById(R.id.tv_open_pic);
        tv_open_qr = (TextView) findViewById(R.id.tv_open_qr);

        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setWindowAnimations(R.style.dialog_anim_bottom);
    }

    public interface TwoSelDialog {
        void topClick();

        void bottomClick();
    }

    public ButtomDialogView setClickListen(final TwoSelDialog mTwoSelDialog) {
        this.mTwoSelDialog = mTwoSelDialog;
        //掃描二維碼
        tv_open_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoSelDialog != null)
                    mTwoSelDialog.topClick();
                if (isShowing())
                    dismiss();
            }
        });
        //打開相冊
        tv_open_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoSelDialog != null)
                    mTwoSelDialog.bottomClick();
                if (isShowing())
                    dismiss();
            }
        });
        return dialog;
    }
}
