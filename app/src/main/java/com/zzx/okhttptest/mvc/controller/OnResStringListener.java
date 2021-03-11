package com.zzx.okhttptest.mvc.controller;

import com.zzx.okhttptest.mvc.model.ResString;

public interface OnResStringListener {
    void onSuccess(ResString result);

    void onFailure();
}
