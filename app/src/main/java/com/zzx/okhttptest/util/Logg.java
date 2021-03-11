package com.zzx.okhttptest.util;

import android.util.Log;

public class Logg {

    private static boolean isDebug = true;

    public Logg() {
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d("react", msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e("react ===== ", msg);
    }
}
