package com.like.base.base.inter;

import java.util.List;

/**
 * 回掉
 */
public interface IPermissonResultListener {
    void onSuccess();

    void onFail(List<String> fail);
}
