package com.llf.android_test.mvc.model;

import com.llf.android_test.mvc.controller.OnResStringListener;

public interface ResStringModel {
    void Login(String userName,String passWord, OnResStringListener onResStringListener);
}
