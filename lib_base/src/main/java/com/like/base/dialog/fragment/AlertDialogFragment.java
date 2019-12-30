package com.like.base.dialog.fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.base.R;
import com.like.base.dialog.BaseDialogFragment;
import com.like.utilslib.image.LoadImageUtil;
import com.like.utilslib.screen.DensityUtil;
import com.like.utilslib.screen.ScreenUtil;

/**
 * 基础的弹出框
 */
public class AlertDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private TextView txt_title;
    private ImageView img_content;
    private TextView txt_msg;
    private Button btn_neg;
    private ImageView img_line;
    private Button btn_pos;

    /*数据*/
    private boolean mOutclickdissmiss = true;
    private boolean mBackKeyeable = true;
    private String mTitle = "";
    private String mContent = "";
    private String mSureBtn = "";
    private String mCancleBtn = "";
    private Object mContentRes = null;
    private View.OnClickListener mSureClick = null;
    private View.OnClickListener mCancleClick = null;

    public static AlertDialogFragment newIntance() {
        AlertDialogFragment fragment = new AlertDialogFragment();
        return fragment;
    }

    @Override
    public Object getResId() {
        return R.layout.dialog_alert_layout;
    }

    @Override
    public void initView() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_content = (ImageView) findViewById(R.id.img_content);
        txt_msg = (TextView) findViewById(R.id.txt_msg);
        btn_neg = (Button) findViewById(R.id.btn_neg);
        img_line = (ImageView) findViewById(R.id.img_line);
        btn_pos = (Button) findViewById(R.id.btn_pos);
    }

    @Override
    public void initData() {
        if (TextUtils.isEmpty(mTitle)) {
            txt_title.setVisibility(View.GONE);
        } else {
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(mTitle);
        }

        if (TextUtils.isEmpty(mContent)) {
            txt_msg.setVisibility(View.GONE);
        } else {
            txt_msg.setVisibility(View.VISIBLE);
            txt_msg.setText(mContent);
        }

        if (mContentRes == null) {
            img_content.setVisibility(View.GONE);
        } else {
            img_content.setVisibility(View.VISIBLE);
            LoadImageUtil.loadImage(img_content, mContentRes);
        }

        if (TextUtils.isEmpty(mSureBtn)) {
            btn_pos.setVisibility(View.GONE);
        } else {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setText(mSureBtn);
            btn_pos.setOnClickListener(this);
        }

        if (TextUtils.isEmpty(mCancleBtn)) {
            btn_neg.setVisibility(View.GONE);
        } else {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setText(mCancleBtn);
            btn_neg.setOnClickListener(this);
        }

        if (!TextUtils.isEmpty(mCancleBtn) && !TextUtils.isEmpty(mSureBtn)) {
            img_line.setVisibility(View.VISIBLE);
        } else {
            img_line.setVisibility(View.GONE);
        }
        setDailogKeyBack(mBackKeyeable);
        setCancelable(mOutclickdissmiss);
    }

    @Override
    public int getViewWidth() {
        return ScreenUtil.getScreenWidth() - DensityUtil.dpTopx(16) * 2;
    }

    @Override
    public int getViewHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getViewGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getAnimationType() {
        return CNTER;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public AlertDialogFragment setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 设置内容
     *
     * @param content
     * @return
     */
    public AlertDialogFragment setContent(String content) {
        mContent = content;
        return this;
    }

    /**
     * 设置内容图片
     *
     * @param object
     * @return
     */
    public AlertDialogFragment setContentImg(Object object) {
        mContentRes = object;
        return this;
    }

    /**
     * 设置确定按钮及事件
     *
     * @param btnText
     * @return
     */
    public AlertDialogFragment setSureBtn(String btnText, final View.OnClickListener clickListener) {
        if (TextUtils.isEmpty(btnText)) {
            btnText = "确定";
        }
        mSureBtn = btnText;
        mSureClick = clickListener;
        return this;
    }

    /**
     * 设置确定按钮及事件
     *
     * @return
     */
    public AlertDialogFragment setSureBtn(final View.OnClickListener clickListener) {
        return setSureBtn("确定", clickListener);
    }

    /**
     * 设置取消按钮及事件
     *
     * @param btnText
     * @return
     */
    public AlertDialogFragment setCancleBtn(String btnText, final View.OnClickListener clickListener) {
        if (TextUtils.isEmpty(btnText)) {
            btnText = "取消";
        }
        mCancleBtn = btnText;
        mCancleClick = clickListener;
        return this;
    }

    /**
     * 设置取消按钮及事件
     *
     * @return
     */
    public AlertDialogFragment setCancleBtn(final View.OnClickListener clickListener) {
        return setCancleBtn("取消", clickListener);
    }

    /**
     * 设置点击区域外是否消息
     *
     * @return
     */
    public AlertDialogFragment setCancleable(boolean iscancle) {
        mOutclickdissmiss = iscancle;
        return this;
    }

    /**
     * 设置返回键是否可用
     *
     * @param backable
     * @return
     */
    public AlertDialogFragment setKeyBackable(boolean backable) {
        mBackKeyeable = backable;
        return this;
    }

    @Override
    public void onClick(View v) {
        onDailogDismiss();
        int i = v.getId();
        if (i == R.id.btn_neg) {
            if (mCancleClick != null) {
                mCancleClick.onClick(v);
            }
        } else if (i == R.id.btn_pos) {   //确定
            if (mSureClick != null) {
                mSureClick.onClick(v);
            }
        }
    }
}
