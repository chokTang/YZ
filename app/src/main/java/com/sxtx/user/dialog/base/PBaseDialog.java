package com.sxtx.user.dialog.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.sxtx.user.R;

public class PBaseDialog extends Dialog {

    public PBaseDialog(Context context) {
        this(context, 0);

    }

    public PBaseDialog(Context context, int themeResId) {
        super(context, themeResId);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除对话框的标题
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0x00000000);
        getWindow().setBackgroundDrawable(gradientDrawable);//设置对话框边框背景,必须在代码中设置对话框背景，不然对话框背景是黑色的

        dimAmount(0.2f);
    }

    public PBaseDialog contentView(@LayoutRes int layoutResID) {
        getWindow().setContentView(layoutResID);
        return this;
    }


    public PBaseDialog contentView(@NonNull View view) {
        getWindow().setContentView(view);
        return this;
    }

    public PBaseDialog contentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        getWindow().setContentView(view, params);
        return this;
    }
    public PBaseDialog layoutParams(@Nullable ViewGroup.LayoutParams params) {
        getWindow().setLayout(params.width, params.height);
        return this;
    }



    /**
     * 在dialog.show()前调用此方法
     * @param mView  dialog要显示的view
     */
    public void setDialogSize(final View mView, final Context context) {
        mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                int heightNow = v.getHeight();//dialog当前的高度
                int widthNow = v.getWidth();//dialog当前的宽度

                Activity activity= (Activity) context;
                int needWidth = (int) (activity.getWindowManager().getDefaultDisplay().getWidth() * 0.7);//最小宽度为屏幕的0.7倍
                int needHeight = (int) (activity.getWindowManager().getDefaultDisplay().getHeight() * 0.6);//最大高度为屏幕的0.6倍
                if (widthNow < needWidth || heightNow > needHeight) {
                    if (widthNow > needWidth) {
                        needWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }
                    if (heightNow < needHeight) {
                        needHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }
                    mView.setLayoutParams(new FrameLayout.LayoutParams(needWidth,
                            needHeight));
                }
            }
        });
    }



    /**
     * 点击外面是否能dissmiss
     *
     * @param canceledOnTouchOutside
     * @return
     */
    public PBaseDialog canceledOnTouchOutside(boolean canceledOnTouchOutside) {
        setCanceledOnTouchOutside(canceledOnTouchOutside);
        return this;
    }

    /**
     * 位置
     *
     * @param gravity
     * @return
     */
    public PBaseDialog gravity(int gravity) {

        getWindow().setGravity(gravity);

        return this;

    }

    /**
     * 偏移
     *
     * @param x
     * @param y
     * @return
     */
    public PBaseDialog offset(int x, int y) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.x = x;
        layoutParams.y = y;

        return this;
    }

    /*
       设置背景阴影,必须setContentView之后调用才生效
        */
    public PBaseDialog dimAmount(float dimAmount) {

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = dimAmount;
        return this;
    }


    /*
   动画类型
    */
    public PBaseDialog animType(PBaseDialog.AnimInType animInType) {


        switch (animInType.getIntType()) {
            case 0:
                getWindow().setWindowAnimations(R.style.dialog_zoom);

                break;
            case 1:
                getWindow().setWindowAnimations(R.style.dialog_anim_left);

                break;
            case 2:
                getWindow().setWindowAnimations(R.style.dialog_anim_top);

                break;
            case 3:
                getWindow().setWindowAnimations(R.style.dialog_anim_right);

                break;
            case 4:
                getWindow().setWindowAnimations(R.style.dialog_anim_bottom);

                break;
        }
        return this;
    }


    /*
    动画类型
     */
    public enum AnimInType {
        CENTER(0),
        LEFT(1),
        TOP(2),
        RIGHT(3),
        BOTTOM(4);

        AnimInType(int n) {
            intType = n;
        }

        final int intType;

        public int getIntType() {
            return intType;
        }
    }
}
