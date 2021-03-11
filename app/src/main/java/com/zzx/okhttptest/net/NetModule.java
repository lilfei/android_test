package com.zzx.okhttptest.net;

import android.content.Context;

public class NetModule {

    private static NetModule instance = null;
    public static Context mContext;

    private NetModule() {
    }

    public static NetModule getInstance() {
        if (instance == null) {
            synchronized (NetModule.class) {
                if (instance == null) {
                    instance = new NetModule();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        mContext = context;
        OkHttpHelper.initOkHttpClient(context);
    }

}
