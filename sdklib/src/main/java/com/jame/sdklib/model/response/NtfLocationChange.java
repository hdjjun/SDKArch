package com.jame.sdklib.model.response;

import android.os.Parcel;

import com.jame.sdklib.model.base.ProtocolBaseModel;
import com.jame.sdklib.model.base.ProtocolIDs;

public class NtfLocationChange extends ProtocolBaseModel {
    private String location;

    public NtfLocationChange(String loc) {
        this.location = loc;
        setProtocolID(ProtocolIDs.NTF_LOCATION_CHANGED);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NtfLocationChange{");
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

    protected NtfLocationChange(Parcel in) {
        super(in);
        this.location = in.readString();
    }

    public static final Creator<NtfLocationChange> CREATOR = new Creator<NtfLocationChange>() {
        @Override
        public NtfLocationChange createFromParcel(Parcel source) {
            return new NtfLocationChange(source);
        }

        @Override
        public NtfLocationChange[] newArray(int size) {
            return new NtfLocationChange[size];
        }
    };
}
