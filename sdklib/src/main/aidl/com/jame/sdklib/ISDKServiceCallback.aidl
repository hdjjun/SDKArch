// ISDKServiceCallback.aidl
package com.jame.sdklib;
import com.jame.sdklib.model.base.TransferModel;
// Declare any non-default types here with import statements

oneway interface ISDKServiceCallback {

    void onAPICallback(in TransferModel callbackModel);

    void onSDKNotify(in TransferModel callbackModel);
}
