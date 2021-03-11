package com.zzx.okhttptest.mvc.model;

import com.zzx.okhttptest.mvc.controller.OnResStringListener;

public interface ResStringModel {
    void Login(String userName,String passWord, OnResStringListener onResStringListener);
}
