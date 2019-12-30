package com.like.utilslib.image.inter;

import java.io.File;

/**
 * 图片压缩接口
 */
public interface ICompressListener {
    void onSuccess(File file);
    void onFail(String msg);
}
