package com.flyjingfish.titlebarlib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

class LogUtils {
    private static final String TAG = "TitleBar";
    private static boolean isGetDebugFlag;
    private static boolean isApkInDebug;
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return true;
        }
    }
    public static boolean isDebug(Context context) {
        if (!isGetDebugFlag){
            isApkInDebug = isApkInDebug(context);
            isGetDebugFlag = true;
        }
        return isApkInDebug;
    }
    public static void logD(String logText){
        if (isApkInDebug){
            Log.d(TAG,logText);
        }
    }
}
