package com.jame.sdklib.model.response;

import android.os.Parcel;

import com.jame.sdklib.model.base.ProtocolBaseModel;

public class RspLocationModel extends ProtocolBaseModel {
    private String location;

    public RspLocationModel(ProtocolBaseModel reqModel) {
        copyBaseInfo(reqModel);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RspLocationModel{");
        sb.append("location='").append(location).append('\'');
        sb.append(", base={").append(super.toString()).append('}');
        sb.append('}');
        return sb.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.location);
    }

    protected RspLocationModel(Parcel in) {
        super(in);
        this.location = in.readString();
    }

    public static final Creator<RspLocationModel> CREATOR = new Creator<RspLocationModel>() {
        @Override
        public RspLocationModel createFromParcel(Parcel source) {
            return new RspLocationModel(source);
        }

        @Override
        public RspLocationModel[] newArray(int size) {
            return new RspLocationModel[size];
        }
    };
}
