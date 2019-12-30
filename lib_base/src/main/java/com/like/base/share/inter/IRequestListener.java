package com.like.base.share.inter;

import android.graphics.Bitmap;

public interface IRequestListener {
    void onRequestSucess(Bitmap bitmap);

    void onRequestFaile();
}
