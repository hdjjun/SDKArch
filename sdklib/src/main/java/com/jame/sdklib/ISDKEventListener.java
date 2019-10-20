package com.jame.sdklib;

import com.jame.sdklib.model.base.ProtocolBaseModel;

public interface ISDKEventListener {
    /**
     * SDK事件回调借口
     * @param eventModel
     */
    void onSDKEvent(ProtocolBaseModel eventModel);
}
