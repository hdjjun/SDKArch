package com.jame.sdklib;

import android.util.Log;

public class SDKLog {
    private static final String TAG = "jame.sdklib";

    public static void d(String logStr){
        Log.d(TAG, logStr);
    }

    public static void e(String logStr, Throwable tr){
        Log.e(TAG, logStr, tr);
    }
}
