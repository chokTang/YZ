package com.sxtx.user.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

/**
 * 根据url创建二维码
 */
public class QrCodeUtil {
    public static Bitmap createQRCodeBitmap(String url, int width, int height) {
        return createQRCodeBitmap(url, width, height, "UTF-8", "H", "0", Color.BLACK, Color.WHITE);
    }

    public static Bitmap createQRCodeBitmap(String content, int width, int height,
                                            String character_set, String error_correction, String margin,
                                            int color_black, int color_white) {

        /** 1.参数合法性判断 */
        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null;
        }

        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null;
        }

        /** 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象 */
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();

        if (!TextUtils.isEmpty(character_set)) {
            hints.put(EncodeHintType.CHARACTER_SET, character_set); // 字符转码格式设置
        }

        if (!TextUtils.isEmpty(error_correction)) {
            hints.put(EncodeHintType.ERROR_CORRECTION, error_correction); // 容错级别设置
        }

        if (!TextUtils.isEmpty(margin)) {
            hints.put(EncodeHintType.MARGIN, margin); // 空白边距设置
        }
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = color_black; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white; // 白色色块像素设置
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象 */
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制
     *
     * @param context
     * @param text
     */
    public static void copy(Context context, String text) {
        try {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
            Toast toast = Toast.makeText(context, "已複製到剪切板", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "操作失敗", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * 复制
     *
     * @param context
     * @param text
     */
    public static void copy(Context context, String text,String toastStr) {
        try {
            ClipboardManager clipboard = (ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
            Toast toast = Toast.makeText(context, toastStr, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "操作失敗", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @SuppressLint("SdCardPath")
    public static void saveImage(final FragmentActivity fragment, Bitmap bitmap) {
        Bitmap cachebmp = bitmap;
        FileOutputStream fos;
        String fileName = "";
        File file;
        try {
            // 判断手机设备是否有SD卡
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // SD卡根目录
                File appDir = new File(Environment.getExternalStorageDirectory(), "Dabaitu");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                fileName = System.currentTimeMillis() + ".jpg";
                file = new File(appDir, fileName);
                fos = new FileOutputStream(file);
            } else
                throw new Exception("創建文件失敗!");

            cachebmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            //通知刷新相册
            MediaStore.Images.Media.insertImage(fragment.getContentResolver(), file.getAbsolutePath(), fileName, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            fragment.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast toast = Toast.makeText(fragment, "保存成功", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }




}
