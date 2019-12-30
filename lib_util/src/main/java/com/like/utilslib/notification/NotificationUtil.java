package com.like.utilslib.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import com.like.utilslib.UtilApp;

/**
 * 通知栏工具类
 *
 * @author longshao
 */
public class NotificationUtil {

    private NotificationManager manager;
    private String mTitle;
    private String mContent;
    @DrawableRes
    private Integer mLargeRes;
    @DrawableRes
    private Integer mSmarllRes;
    private long mNotificationTime;

    private NotificationUtil(Bulider bulider) {
        manager = (NotificationManager) UtilApp.getIntance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mTitle = bulider.mTitle;
        mContent = bulider.mContent;
        mLargeRes = bulider.mLargeRes;
        mSmarllRes = bulider.mSmarllRes;
        mNotificationTime = bulider.mNotificationTime;
    }

    public static class Bulider {
        private String mTitle;
        private String mContent;
        @DrawableRes
        private Integer mLargeRes;
        @DrawableRes
        private Integer mSmarllRes;
        private long mNotificationTime = System.currentTimeMillis();

        public Bulider setmTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Bulider setmContent(String mContent) {
            this.mContent = mContent;
            return this;
        }

        public Bulider setmLargeRes(Integer mLargeRes) {
            this.mLargeRes = mLargeRes;
            return this;
        }

        public Bulider setmSmarllRes(Integer mSmarllRes) {
            this.mSmarllRes = mSmarllRes;
            return this;
        }

        public Bulider setmNotificationTime(long mNotificationTime) {
            this.mNotificationTime = mNotificationTime;
            return this;
        }

        public NotificationUtil build() {
            return new NotificationUtil(this);
        }
    }

    public void show(int notificationId,String channelId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(UtilApp.getIntance().getApplicationContext(), channelId);
        /**
         *  设置Builder
         */
        //设置标题
        mBuilder.setContentTitle(mTitle)
                //设置内容
                .setContentText(mContent)
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(UtilApp.getIntance().getApplicationContext().getResources(), mLargeRes))
                //设置小图标
                .setSmallIcon(mSmarllRes)
                //设置通知时间
                .setWhen(mNotificationTime)
                //首次进入时显示效果
                .setTicker(mContent)
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        manager.notify(notificationId, mBuilder.build());
    }
}
