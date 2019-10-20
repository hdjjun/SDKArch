package com.jame.sdkservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.SparseIntArray;

import com.jame.sdklib.ISDKService;
import com.jame.sdklib.ISDKServiceCallback;
import com.jame.sdklib.model.base.ProtocolBaseModel;
import com.jame.sdklib.model.base.TransferModel;
import com.jame.sdklib.model.response.NtfLocationChange;

public class SDKService extends Service implements APIManager.APICallbackListener {
    private RemoteCallbackList<ISDKServiceCallback> mServiceCallbacks = new RemoteCallbackList<>();
    private APIManager mAPIManager = new APIManager(this);
    private SparseIntArray mAPICallingIdMap = new SparseIntArray();

    private ISDKService.Stub mSDKServiceBinder = new ISDKService.Stub() {
        @Override
        public int registerCallback(ISDKServiceCallback callback) throws RemoteException {
            SDKServiceLog.d(String.format("registerCallback calluid:%s pid:%s", Binder.getCallingUid(), Binder.getCallingPid()));
            boolean registered = mServiceCallbacks.register(callback, Binder.getCallingPid());
            return registered ? 0 : -1;
        }

        @Override
        public int unregisterCallback(ISDKServiceCallback callback) throws RemoteException {
            SDKServiceLog.d(String.format("unregisterCallback calluid:%s pid:%s", Binder.getCallingUid(), Binder.getCallingPid()));
            boolean unregistered = mServiceCallbacks.unregister(callback);
            return unregistered ? 0 : -1;
        }

        @Override
        public TransferModel invokeSDKAPI(TransferModel transferModel) throws RemoteException {
            SDKServiceLog.d("SDKService => invokeSDKAPI:" + transferModel.getProtocolBaseModel());
            ProtocolBaseModel result = mAPIManager.callAPI(transferModel.getProtocolBaseModel());
            TransferModel resultTransferModel = new TransferModel();
            resultTransferModel.setProtocolBaseModel(result);
            return resultTransferModel;
        }

        @Override
        public void invokeSDKAPIAsync(TransferModel transferModel) throws RemoteException {
            SDKServiceLog.d("SDKService => invokeSDKAPIAsync:" + transferModel.getProtocolBaseModel());
            synchronized (mAPICallingIdMap){
                mAPICallingIdMap.put(transferModel.getProtocolBaseModel().getCallbackId(), Binder.getCallingPid());
            }
            mAPIManager.callAPIAsync(transferModel.getProtocolBaseModel());

        }
    };

    public SDKService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSDKServiceBinder;
    }


    @Override
    public void onAPICallback(ProtocolBaseModel resultModel) {
        int callingPid = mAPICallingIdMap.get(resultModel.getCallbackId(), -1);
        SDKServiceLog.d( "onAPICallback: callingPid:" + callingPid + "; " + resultModel);
        if (callingPid == -1) return;
        synchronized (mServiceCallbacks) {
            try {
                for (int i = mServiceCallbacks.beginBroadcast() - 1; i >= 0; i--) {
                    if ((int) mServiceCallbacks.getBroadcastCookie(i) == callingPid) {
                        ISDKServiceCallback sdkCallback = mServiceCallbacks.getBroadcastItem(i);
                        TransferModel transferModel = new TransferModel();
                        transferModel.setProtocolBaseModel(resultModel);
                        sdkCallback.onAPICallback(transferModel);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mServiceCallbacks.finishBroadcast();
            }
        }
    }

    /**
     * Service主动通知客户端
     * @param notifyEventModel
     */
    private void notifyServiceEvent(ProtocolBaseModel notifyEventModel){
        synchronized (mServiceCallbacks) {
            try {
                TransferModel transferModel = new TransferModel();
                transferModel.setProtocolBaseModel(notifyEventModel);
                SDKServiceLog.d( "notifyServiceEvent:" + notifyEventModel);
                for (int i = mServiceCallbacks.beginBroadcast() - 1; i >= 0; i--) {
                    ISDKServiceCallback sdkCallback = mServiceCallbacks.getBroadcastItem(i);
                    sdkCallback.onSDKNotify(transferModel);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mServiceCallbacks.finishBroadcast();
            }
        }
    }
}
