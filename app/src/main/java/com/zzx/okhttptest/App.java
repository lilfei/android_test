package com.zzx.okhttptest;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.zzx.okhttptest.net.NetModule;

import java.util.List;

public class App extends Application {
    @Override
    public void onCreate() {

        String processName = getProcessName(this, android.os.Process.myPid());
        if (processName.endsWith("com.zzw.App")) {
            // TODO: 2019/7/31
        }

        super.onCreate();
        NetModule.getInstance().init(getApplicationContext());
    }

    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningApps) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
