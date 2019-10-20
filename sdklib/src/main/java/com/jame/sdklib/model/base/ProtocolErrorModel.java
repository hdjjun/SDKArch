package com.jame.sdklib.model.base;

import android.os.Parcel;

/**
 * Navi api调用失败回传信息
 */

public class ProtocolErrorModel extends ProtocolBaseModel {
    private int errorCode;
    private String errorString;

    /**
     * 获得错误编号
     * @return
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 设置错误编号
     * @param errorCode
     */
    public ProtocolErrorModel setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        this.errorString = ProtocolResultCode.getErrorMsg(errorCode);
        return this;
    }

    /**
     * 获取错误描述
     * @return
     */
    public String getErrorString() {
        return errorString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.errorCode);
        dest.writeString(this.errorString);
    }

    public ProtocolErrorModel(ProtocolBaseModel reqModel) {
        copyBaseInfo(reqModel);
    }

    protected ProtocolErrorModel(Parcel in) {
        super(in);
        this.errorCode = in.readInt();
        this.errorString = in.readString();
    }

    public static final Creator<ProtocolErrorModel> CREATOR = new Creator<ProtocolErrorModel>() {
        @Override
        public ProtocolErrorModel createFromParcel(Parcel source) {
            return new ProtocolErrorModel(source);
        }

        @Override
        public ProtocolErrorModel[] newArray(int size) {
            return new ProtocolErrorModel[size];
        }
    };

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProtocolErrorModel{");
        sb.append("errorCode=").append(errorCode);
        sb.append(", errorString='").append(errorString).append('\'');
        sb.append("{base=").append(super.toString()).append("}; ");
        sb.append('}');
        return sb.toString();
    }
}
