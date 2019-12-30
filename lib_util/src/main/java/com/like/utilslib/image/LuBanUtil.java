package com.like.utilslib.image;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.like.utilslib.UtilApp;
import com.like.utilslib.image.inter.ICompressListener;

import java.io.File;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 鲁班压缩工具类
 * 备注：主要是用于处理图片压缩的
 */
public class LuBanUtil {

    public static void compress(File file, final ICompressListener listener){
        Luban.with(UtilApp.getIntance().getApplicationContext())
                .load(file)
                .ignoreBy(1024)//单位是KB 1M以下不压缩
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setTargetDir(Environment.getExternalStorageDirectory().getPath() + "/" + UtilApp.getIntance().getApplicationContext().getPackageName())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        if (listener!=null){
                            listener.onSuccess(file);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener!=null){
                            listener.onFail(e.getMessage());
                        }
                    }
                })
                .launch();
    }

    public static void compress(String file, final ICompressListener listener){
        Luban.with(UtilApp.getIntance().getApplicationContext())
                .load(file)
                .ignoreBy(1024)//单位是KB 1M以下不压缩
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setTargetDir(Environment.getExternalStorageDirectory().getPath() + "/" + UtilApp.getIntance().getApplicationContext().getPackageName())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        if (listener!=null){
                            listener.onSuccess(file);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener!=null){
                            listener.onFail(e.getMessage());
                        }
                    }
                })
                .launch();
    }

    public static void compress(Uri file, final ICompressListener listener){
        Luban.with(UtilApp.getIntance().getApplicationContext())
                .load(file)
                .ignoreBy(1024)//单位是KB 1M以下不压缩
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setTargetDir(Environment.getExternalStorageDirectory().getPath() + "/" + UtilApp.getIntance().getApplicationContext().getPackageName())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        if (listener!=null){
                            listener.onSuccess(file);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener!=null){
                            listener.onFail(e.getMessage());
                        }
                    }
                })
                .launch();
    }
}
