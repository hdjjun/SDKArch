// ISDKService.aidl
package com.jame.sdklib;
import com.jame.sdklib.ISDKServiceCallback;
import com.jame.sdklib.model.base.TransferModel;

// Declare any non-default types here with import statements

interface ISDKService {
    int registerCallback(in ISDKServiceCallback callback);

    int unregisterCallback(in ISDKServiceCallback callback);

    TransferModel invokeSDKAPI(in TransferModel transferModel);

    void invokeSDKAPIAsync(in TransferModel transferModel);
}
