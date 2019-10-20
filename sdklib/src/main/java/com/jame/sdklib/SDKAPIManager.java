package com.jame.sdklib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.jame.sdklib.model.base.ProtocolBaseModel;
import com.jame.sdklib.model.base.ProtocolErrorModel;
import com.jame.sdklib.model.base.ProtocolResultCode;
import com.jame.sdklib.model.base.TransferModel;
import com.jame.sdklib.model.request.ReqLocationModel;

import java.util.concurrent.ConcurrentHashMap;


public class SDKAPIManager {
    private Context mContext;
    private static final ComponentName SDK_SERVICE_COMPONENT =
            new ComponentName("com.jame.sdkservice", "com.jame.sdkservice.SDKService");
    private ISDKEventListener mSDKEventListener;
    private ConcurrentHashMap<Integer, IAPICallback> mAPICallbackList = new ConcurrentHashMap<>();
    private ISDKService mSDKService;

    private static class SDKAPIManagerHolder{
        private static final SDKAPIManager sInstance = new SDKAPIManager();
    }

    private ISDKServiceCallback.Stub mSDKServiceCallback = new ISDKServiceCallback.Stub() {
        @Override
        public void onAPICallback(TransferModel callbackModel) throws RemoteException {
            ProtocolBaseModel baseModel = callbackModel.getProtocolBaseModel();
            IAPICallback callback = mAPICallbackList.remove(baseModel.getCallbackId());
            SDKLog.d("onAPICallback:"+baseModel+"; callback:"+callback);
            if (baseModel instanceof ProtocolErrorModel) {
                ProtocolErrorModel errorModel = (ProtocolErrorModel) baseModel;
                if (callback != null) {
                    callback.onError(errorModel);
                }
            } else {
                if (callback != null) {
                    callback.onSuccess(baseModel);
                }
            }
        }

        @Override
        public void onSDKNotify(TransferModel callbackModel) throws RemoteException {
            if(null != mSDKEventListener){
                mSDKEventListener.onSDKEvent(callbackModel.getProtocolBaseModel());
            }
        }
    };

    private IBinder.DeathRecipient mBindDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            SDKLog.d("binder dided, rebind");
            if(mSDKService != null){
                mSDKService.asBinder().unlinkToDeath(mBindDeathRecipient, 0);
                mSDKService = null;
            }
            bindSDKService();
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
            SDKLog.d("on SDK service connected");
            mSDKService = ISDKService.Stub.asInterface(iBinder);
            try {
                mSDKService.registerCallback(mSDKServiceCallback);
            }catch (Throwable tr){
                SDKLog.e("sdk service connected, but registerCallback occur exception", tr);
            }
            try {
                iBinder.linkToDeath(mBindDeathRecipient, 0);
            }catch (Throwable tr){
                SDKLog.e("sdk service connected, but linktoDeath occur exception", tr);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            SDKLog.d("on SDK service disconnected");
            mSDKService = null;
        }
    };

    public static SDKAPIManager getInstance(){
        return SDKAPIManagerHolder.sInstance;
    }

    public void init(Context context){
        this.mContext = context.getApplicationContext();
        bindSDKService();
    }

    /**
     * 使用同步方法获取location
     * @return
     */
    public ProtocolBaseModel getLocation(){
        ReqLocationModel reqLocationModel = new ReqLocationModel();
        return invokeSDKAPI(reqLocationModel);
    }

    /**
     * 使用异步方法获取location
     */
    public void getLocationAsync(IAPICallback callback){
        ReqLocationModel reqLocationModel = new ReqLocationModel();
        invokeSDKAPIAsync(reqLocationModel, callback);
    }

    /**
     * 同步API方法
     * @param reqModel
     * @return
     */
    public ProtocolBaseModel invokeSDKAPI(ProtocolBaseModel reqModel){
        if(!isServiceConnected())
            throw new RuntimeException("Service not connected");
        SDKLog.d("invokeSDKAPI："+reqModel);
        reqModel.setPackageName(mContext.getPackageName());
        TransferModel transferModel = new TransferModel();
        transferModel.setProtocolBaseModel(reqModel);
        try {
            TransferModel result = mSDKService.invokeSDKAPI(transferModel);
            return result.getProtocolBaseModel();
        }catch (Throwable tr){
            SDKLog.e("invokeSDKAPI ex", tr);
            return new ProtocolErrorModel(null).setErrorCode(ProtocolResultCode.RESULT_FAIL);
        }
    }

    /**
     * 异步调用API方法
     * @param reqModel
     * @param callback
     */
    public void invokeSDKAPIAsync(ProtocolBaseModel reqModel, IAPICallback callback){
        if(!isServiceConnected())
            throw new RuntimeException("Service not connected");

        reqModel.setPackageName(mContext.getPackageName());
        TransferModel transferModel = new TransferModel();
        transferModel.setProtocolBaseModel(reqModel);
        SDKLog.d("invokeSDKAPIAsync："+reqModel+"; callback:"+callback);

        if (null != callback) {
            mAPICallbackList.put(reqModel.getCallbackId(), callback);
        }
        try {
            mSDKService.invokeSDKAPIAsync(transferModel);
        }catch (Throwable tr){
            SDKLog.e("invokeSDKAPIAsync ex", tr);
        }
    }


    /**
     * 服务是否连上
     * @return
     */
    public boolean isServiceConnected(){
        return (mSDKService != null &&
                mSDKService.asBinder().isBinderAlive());
    }

    public void setSDKEventListener(ISDKEventListener listener){
        mSDKEventListener = listener;
    }

    private void bindSDKService(){
        Intent intent = new Intent();
        intent.setComponent(SDK_SERVICE_COMPONENT);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


}
