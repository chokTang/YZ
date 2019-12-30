package com.sxtx.user.fragment.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.like.base.base.BaseToolbarFragment;
import com.like.base.base.presenter.BasePresenter;
import com.like.utilslib.app.CommonUtil;
import com.like.utilslib.file.FileUtil;
import com.sxtx.user.inter.ISelectPictureListener;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.model.TakePhotoOptions;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的图片选取封装类
 * Created by Administrator on 2018/4/13.
 */

public abstract class BasePhotoFragment<P extends BasePresenter> extends BaseToolbarFragment<P>
        implements TakePhoto.TakeResultListener, InvokeListener {

    private InvokeParam invokeParam;
    private TakePhoto takePhoto;

    private ISelectPictureListener mListener;

    private int type_select = 0;
    private final int SELECT_PHOTO = 0x352;//从相册选择一张图片
    private final int SELECT_PHOTO_CORP = 0x353;//从相册选择一张图片裁剪并且压缩
    private final int SELECT_CAMERA = 0x354;//从相机选择一张图片
    private final int SELECT_CAMERA_CORP = 0x355;//从相机选择一张图片裁剪并且压缩
    private final int SELECT_MULTIPLE = 0x356;//从相机选择多张图片,不裁剪，不压缩
    private final int defaultWidth = 600;//默认的宽度
    private final int defaultHeight = 601;//默认的宽度

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getBaseActivity(), type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(final TResult result) {
        //特别说明一下 getOriginalPath----是原图路径，getCompressPath--是压缩成功以后的路径
        getBaseActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    switch (type_select) {
                        case SELECT_PHOTO:
                        case SELECT_CAMERA:
                            mListener.onSuccess(result.getImage().getOriginalPath());
                            break;
                        case SELECT_PHOTO_CORP:
                        case SELECT_CAMERA_CORP:
                            mListener.onSuccess(result.getImage().getCompressPath());
                            break;
                        //多张图片的时候
                        case SELECT_MULTIPLE:
                            List<TImage> images = result.getImages();
                            if (images != null && images.size() > 0) {
                                List<String> mlist = new ArrayList<>();
                                for (TImage image : images) {
                                    mlist.add(image.getOriginalPath());
                                }
                                if (mlist.size() > 0) {
                                    mListener.onSuccess(CommonUtil.ListToString(mlist));
                                }
                            }
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void takeFail(TResult result, final String msg) {
        getBaseActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
                if (mListener != null) {
                    mListener.onFail();
                }
            }
        });
    }

    @Override
    public void takeCancel() {
    }

    /**
     * 从相册选择一张图片
     *
     * @param listener
     */
    protected void onChooseFromPhoto(ISelectPictureListener listener) {
        type_select = SELECT_PHOTO;
        mListener = listener;
        configTakePhotoOption(getTakePhoto());
        getTakePhoto().onPickFromGallery();
    }

    /**
     * 从相机中拍照一张图片
     *
     * @param listener
     */
    protected void onChooseFromCamera(ISelectPictureListener listener) {
        Uri uri = Uri.fromFile(FileUtil.createPhotoFile(System.currentTimeMillis() + ".jpg"));
        type_select = SELECT_CAMERA;
        mListener = listener;
        configTakePhotoOption(getTakePhoto());
        getTakePhoto().onPickFromCapture(uri);
    }

    /**
     * 从相册选择一张图片并裁剪
     *
     * @param listener
     */
    protected void onChooseFromPhotoWithCrop(ISelectPictureListener listener) {
        onChooseFromPhotoWithCrop(defaultWidth, defaultHeight, listener);
    }

    /**
     * 拍照 并裁剪
     *
     * @param listener
     */
    protected void onChooseFromCameraWithCorp(ISelectPictureListener listener) {
        onChooseFromCameraWithCorp(defaultWidth, defaultHeight, listener);
    }

    /**
     * 从相册选择一张图片并裁剪
     *
     * @param width    图片输出的宽度
     * @param height   图片输出的高度
     * @param listener
     */
    protected void onChooseFromPhotoWithCrop(int width, int height, ISelectPictureListener listener) {
        type_select = SELECT_PHOTO_CORP;
        mListener = listener;
        Uri uri = Uri.fromFile(FileUtil.createPhotoFile(System.currentTimeMillis() + ".jpg"));
        configCompress(getTakePhoto());
        configTakePhotoOption(getTakePhoto());
        getTakePhoto().onPickFromGalleryWithCrop(uri, getRoundCropOptions(width, height));
    }

    /**
     * 拍照 并裁剪
     *
     * @param width    图片输出的宽度
     * @param height   图片输出的高度
     * @param listener
     */
    protected void onChooseFromCameraWithCorp(int width, int height, ISelectPictureListener listener) {
        type_select = SELECT_CAMERA_CORP;
        mListener = listener;
        Uri uri = Uri.fromFile(FileUtil.createFile(getContext().getPackageName(), System.currentTimeMillis() + ".jpg"));
        configCompress(getTakePhoto());
        configTakePhotoOption(getTakePhoto());
        getTakePhoto().onPickFromCaptureWithCrop(uri, getRoundCropOptions(width, height));
    }

    /**
     * 选择多张图片
     *
     * @param number   最多多少张图片
     * @param listener
     */
    protected void onChooseMultiple(int number, ISelectPictureListener listener) {
        type_select = SELECT_MULTIPLE;
        mListener = listener;
        configTakePhotoOption(getTakePhoto());
        getTakePhoto().onPickMultiple(number);
    }

    /**
     * 选择多张图片并且裁剪 默认的宽高
     *
     * @param number   最多多少张图片
     * @param listener
     */
    protected void onChooseMultipleWithCrop(int number, ISelectPictureListener listener) {
        onChooseMultipleWithCrop(number, defaultWidth, defaultHeight, listener);
    }

    /**
     * 选择多张图片并且裁剪
     *
     * @param number   最多多少张图片
     * @param width    图片裁剪的宽度
     * @param height   图片裁剪的高度
     * @param listener
     */
    protected void onChooseMultipleWithCrop(int number, int width, int height, ISelectPictureListener listener) {
        type_select = SELECT_MULTIPLE;
        mListener = listener;
        configCompress(getTakePhoto());
        configTakePhotoOption(getTakePhoto());
        getTakePhoto().onPickMultipleWithCrop(number, getRoundCropOptions(width, height));
    }

    /**
     * 获取裁剪配置
     *
     * @param width  图片输出的宽度
     * @param height 图片输出的高度
     * @return
     */
    private CropOptions getRoundCropOptions(int width, int height) {
        if (width == height) {
            height += 1;//如果不控制 setAspectX setAspectY这2个参数，则裁剪的时候是随意大小。
        }
        CropOptions options = new CropOptions.Builder()
                .setAspectX(width)
                .setAspectY(height)
                .setOutputX(width)
                .setOutputY(height)
                .setWithOwnCrop(false)//使用裁剪工具 false是第三方的（目测是手机内置的裁剪工具），true是本框架自带的裁剪
                .create();
        return options;
    }

    /**
     * 设置压缩配置
     */
    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 1024 * 1024;//压缩的最大值,1M
        int width = 1200;//最大的宽度 px
        int height = 1200;//最大的高度 px
        CompressConfig config;
        LubanOptions option = new LubanOptions.Builder().setMaxHeight(height).setMaxWidth(width).setMaxSize(maxSize).create();
        config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(true);//是否保留原图
        takePhoto.onEnableCompress(config, true);
    }

    /**
     * 配置一下photo的文件
     *
     * @param takePhoto
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);//是否使用自带的takePhoto相册
        builder.setCorrectImage(false);//是否纠正图片拍照的角度
        takePhoto.setTakePhotoOptions(builder.create());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
