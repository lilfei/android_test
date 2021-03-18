package com.llf.android_test.mvc.controller;

import com.llf.android_test.mvc.model.ResString;

public interface OnResStringListener {
    void onSuccess(ResString result);

    void onFailure();
}
