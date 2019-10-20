package com.jame.sdkservice;

import android.util.Log;

public class SDKServiceLog {
    private static final String TAG = "jame.sdkservice";

    public static void d(String logStr){
        Log.d(TAG, logStr);
    }

    public static void e(String logStr, Throwable tr){
        Log.e(TAG, logStr, tr);
    }
}
