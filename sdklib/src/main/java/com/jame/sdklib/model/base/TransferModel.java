package com.jame.sdklib.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.jame.sdklib.SDKLog;

/**
 * 用于client 与 server之间传输的类
 */

public class TransferModel<T extends ProtocolBaseModel> implements Parcelable {

    private T protocolBaseModel;

    public T getProtocolBaseModel() {
        return protocolBaseModel;
    }

    public void setProtocolBaseModel(T protocolBaseModel) {
        this.protocolBaseModel = protocolBaseModel;
        if(protocolBaseModel.getCallbackId() == 0){
            protocolBaseModel.setCallbackId(protocolBaseModel.genRandomId());
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.protocolBaseModel.getClass().getName());
        dest.writeParcelable(this.protocolBaseModel, flags);
    }

    public TransferModel() {
    }

    protected TransferModel(Parcel in) {
        String modelCls = in.readString();
        try {
            this.protocolBaseModel = in.readParcelable(Class.forName(modelCls).getClassLoader());
        }catch (Exception ex){
            SDKLog.e("createTransferModel from parcel cls:"+modelCls, ex);
        }
    }

    public static final Creator<TransferModel> CREATOR = new Creator<TransferModel>() {
        @Override
        public TransferModel createFromParcel(Parcel source) {
            return new TransferModel(source);
        }

        @Override
        public TransferModel[] newArray(int size) {
            return new TransferModel[size];
        }
    };
}
