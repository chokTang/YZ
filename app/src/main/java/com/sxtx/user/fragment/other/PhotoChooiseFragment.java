package com.sxtx.user.fragment.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.like.utilslib.app.CommonUtil;
import com.like.utilslib.image.LoadImageUtil;
import com.sxtx.user.R;
import com.sxtx.user.fragment.base.BasePhotoFragment;
import com.sxtx.user.inter.ISelectPictureListener;

import java.util.List;

public class PhotoChooiseFragment extends BasePhotoFragment implements View.OnClickListener {

    private Button btn_camal, btn_photo, btn_camal_1, btn_photo_1, btn_photo_more, btn_photo_more_1;
    private ImageView img_camer, img_photo, img_camer_1, img_photo_1, img_photo_more_1,img_photo_more_2;

    @Override
    public Object getResId() {
        return R.layout.fragment_photo_chooise;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        onBack();
        setToolbarTitle("图片选择");
        btn_camal = (Button) $(R.id.btn_camal);
        btn_photo = (Button) $(R.id.btn_photo);
        btn_camal_1 = (Button) $(R.id.btn_camal_1);
        btn_photo_1 = (Button) $(R.id.btn_photo_1);
        btn_photo_more = (Button) $(R.id.btn_photo_more);
        btn_photo_more_1 = (Button) $(R.id.img_camer);
        img_photo = (ImageView) $(R.id.img_photo);
        img_camer_1 = (ImageView) $(R.id.img_camer_1);
        img_photo_1 = (ImageView) $(R.id.img_photo_more_1);
        img_photo_more_2 = (ImageView) $(R.id.img_photo_more_2);

        btn_camal.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
        btn_camal_1.setOnClickListener(this);
        btn_photo_1.setOnClickListener(this);
        btn_photo_more.setOnClickListener(this);
        btn_photo_more_1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camal:
                onChooseFromCamera(new ISelectPictureListener() {
                    @Override
                    public void onSuccess(final String imgUrl) {
                        LoadImageUtil.loadImage(img_camer, imgUrl);
                    }

                    @Override
                    public void onFail() {

                    }
                });
                break;
            case R.id.btn_photo:
                onChooseFromPhoto(new ISelectPictureListener() {
                    @Override
                    public void onSuccess(final String imgUrl) {
                        LoadImageUtil.loadImage(img_photo, imgUrl);
                    }

                    @Override
                    public void onFail() {

                    }
                });
                break;
            //拍照并裁剪
            case R.id.btn_camal_1:
                onChooseFromCameraWithCorp(new ISelectPictureListener() {
                    @Override
                    public void onSuccess(final String imgUrl) {
                        LoadImageUtil.loadImage(img_camer_1, imgUrl);
                    }

                    @Override
                    public void onFail() {
                    }
                });
                break;
            //相册中选取一张并且裁剪
            case R.id.btn_photo_1:
                onChooseFromPhotoWithCrop(new ISelectPictureListener() {
                    @Override
                    public void onSuccess(String imgUrl) {
                        LoadImageUtil.loadImage(img_photo_1, imgUrl);
                    }

                    @Override
                    public void onFail() {

                    }
                });
                break;
            //选取多张
            case R.id.btn_photo_more:
                onChooseMultiple(2, new ISelectPictureListener() {
                    @Override
                    public void onSuccess(String imgUrl) {
                        List<String> moreList = CommonUtil.StringToList(imgUrl);
                        LoadImageUtil.loadImage(img_photo_more_1, moreList.get(0).toString());
                    }

                    @Override
                    public void onFail() {

                    }
                });
                break;
            //选取多张并且裁剪
            case R.id.btn_photo_more_1:
                onChooseMultipleWithCrop(2, new ISelectPictureListener() {
                    @Override
                    public void onSuccess(String imgUrl) {
                        List<String> moreList = CommonUtil.StringToList(imgUrl);
                        LoadImageUtil.loadImage(img_photo_more_2, moreList.get(0).toString());
                    }

                    @Override
                    public void onFail() {

                    }
                });
                break;
        }
    }
}
