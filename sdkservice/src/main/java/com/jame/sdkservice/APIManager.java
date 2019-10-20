package com.jame.sdkservice;

import com.jame.sdklib.model.base.ProtocolBaseModel;
import com.jame.sdklib.model.base.ProtocolErrorModel;
import com.jame.sdklib.model.base.ProtocolIDs;
import com.jame.sdklib.model.base.ProtocolResultCode;
import com.jame.sdklib.model.request.ReqLocationModel;
import com.jame.sdklib.model.response.RspLocationModel;

public class APIManager {
    private APICallbackListener mAPICallbackListener;
    interface APICallbackListener{
        void onAPICallback(ProtocolBaseModel resultModel);
    }

    public APIManager(APICallbackListener listener){
        mAPICallbackListener = listener;
    }


    public ProtocolBaseModel callAPI(ProtocolBaseModel reqModel){
        SDKServiceLog.d("callAPI:"+reqModel);
        switch(reqModel.getProtocolID()){
            case ProtocolIDs
                    .GET_LAST_KNOWN_LOCATION:
                ReqLocationModel reqLocationModel = (ReqLocationModel) reqModel;
                RspLocationModel rspLocationModel = new RspLocationModel(reqLocationModel);
                rspLocationModel.setLocation("This is from service");
                return rspLocationModel;
            default:
                ProtocolErrorModel errorModel = new ProtocolErrorModel(reqModel);
                errorModel.setErrorCode(ProtocolResultCode.RESULT_UNSUPPORT);
                return errorModel;
        }
    }

    public void callAPIAsync(ProtocolBaseModel reqModel){
        SDKServiceLog.d("callAPIAsync:"+reqModel);
        switch(reqModel.getProtocolID()) {
            case ProtocolIDs
                    .GET_LAST_KNOWN_LOCATION:
                ReqLocationModel reqLocationModel = (ReqLocationModel) reqModel;
                RspLocationModel rspLocationModel = new RspLocationModel(reqLocationModel);
                rspLocationModel.setLocation("This is from service async");
                apiCallback(rspLocationModel);
                break;
            default:
                ProtocolErrorModel errorModel = new ProtocolErrorModel(reqModel);
                errorModel.setErrorCode(ProtocolResultCode.RESULT_UNSUPPORT);
                apiCallback(errorModel);
                break;
        }
    }

    private void apiCallback(ProtocolBaseModel result){
        if(null != mAPICallbackListener){
            mAPICallbackListener.onAPICallback(result);
        }
    }
}