package com.jame.sdklib.model.request;

import android.os.Parcel;

import com.jame.sdklib.model.base.ProtocolBaseModel;
import com.jame.sdklib.model.base.ProtocolIDs;

public class ReqLocationModel extends ProtocolBaseModel {
    public ReqLocationModel() {
        setProtocolID(ProtocolIDs.GET_LAST_KNOWN_LOCATION);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected ReqLocationModel(Parcel in) {
        super(in);
    }

    public static final Creator<ReqLocationModel> CREATOR = new Creator<ReqLocationModel>() {
        @Override
        public ReqLocationModel createFromParcel(Parcel source) {
            return new ReqLocationModel(source);
        }

        @Override
        public ReqLocationModel[] newArray(int size) {
            return new ReqLocationModel[size];
        }
    };
}
