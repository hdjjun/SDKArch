package com.jame.sdklib.model.base;

import android.util.SparseArray;


/**
 * SDK API 调用结果码
 */

public class ProtocolResultCode {
    public static final int RESULT_OK = 0;
    public static final int RESULT_FAIL = 1001;
    public static final int RESULT_OP_TIMEOUT = 1002;
    public static final int RESULT_UNSUPPORT = 1003;
    private static final SparseArray<String> RESULT_MESSAGE = new SparseArray<String>();


    static {
        RESULT_MESSAGE.put(RESULT_OK, "请求成功");
        RESULT_MESSAGE.put(RESULT_FAIL, "请求失败");
        RESULT_MESSAGE.put(RESULT_OP_TIMEOUT, "请求超时");
        RESULT_MESSAGE.put(RESULT_UNSUPPORT, "命令不支持");
    }

    public static String getErrorMsg(int errorCode){
        return RESULT_MESSAGE.get(errorCode,"");
    }
}
