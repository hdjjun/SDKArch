package com.jame.sdklib;

import com.jame.sdklib.model.base.ProtocolBaseModel;
import com.jame.sdklib.model.base.ProtocolErrorModel;

public interface IAPICallback<T> {
    void onSuccess(T model);
    void onError(ProtocolErrorModel errModel);
}
