package com.jame.sdklib.model.base;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;


import java.util.Random;

/**
 * SDK API 协议基类
 */
public class ProtocolBaseModel implements Parcelable,Cloneable {
    private int protocolID = 0;
    private int callbackId = 0;
    private int protocolVersion = 1;
    private String packageName = "";
    private Bundle extraData;
    private long timeout = 0L;

    protected void copyBaseInfo(ProtocolBaseModel otherModel){
        if(null != otherModel){
            setPackageName(otherModel.getPackageName());
            setProtocolID(otherModel.getProtocolID());
            setCallbackId(otherModel.getCallbackId());
        }
    }

    /**
     * 获得调用者包名
     * @return
     */
    public String getPackageName() {
        return this.packageName;
    }

    /**
     * 设置调用者包名
     * @param pkg
     */
    public void setPackageName(String pkg) {
        this.packageName = pkg;
    }

    /**
     * 获取协议版本
     * @return
     */
    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    /**
     * 设置协议版本
     * @param version
     */
    public void setProtocolVersion(int version) {
        this.protocolVersion = version;
    }

    /**
     * 获取调用id,用于鉴别哪一次调用
     * @return
     */
    public int getCallbackId() {
        return this.callbackId;
    }

    /**
     * 设置调用id,用于鉴别哪一次调用
     * @param id
     */
    public void setCallbackId(int id) {
        this.callbackId = id;
    }

    /**
     * 获得协议id
     * @return
     */
    public int getProtocolID() {
        return this.protocolID;
    }

    /**
     * 设置协议id,相当于指定调用哪个API
     * @param protocolID
     */
    public void setProtocolID(int protocolID) {
        this.protocolID = protocolID;
    }

    /**
     * 获取扩展信息
     * @return
     */
    public Bundle getExtraData() {
        return extraData;
    }

    /**
     * 设置扩展信息
     * @param extraData
     */
    public void setExtraData(Bundle extraData) {
        this.extraData = extraData;
    }

    /**
     * 获取超时时间，单位毫秒
     * @return
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * 设置超时时间，单位毫秒
     * @param timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * 生成随机id
     * @return
     */
    public int genRandomId() {
        int result = protocolID;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + new Random(SystemClock.elapsedRealtimeNanos()).nextInt();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProtocolBaseModel{");
        sb.append("protocolID=").append(protocolID);
        sb.append(", callbackId=").append(callbackId);
        sb.append(", protocolVersion='").append(protocolVersion).append('\'');
        sb.append(", packageName='").append(packageName).append('\'');
        sb.append(", timeout=").append(timeout);
        sb.append(", extraData=").append(extraData);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public ProtocolBaseModel clone() {
        ProtocolBaseModel cloneModel = null;
        try{
            cloneModel = (ProtocolBaseModel) super.clone();
            if(null != this.extraData) {
                cloneModel.extraData = (Bundle) this.extraData.clone();
            }
        }catch (CloneNotSupportedException ex){
            cloneModel = new ProtocolBaseModel();
        }
        return cloneModel;
    }


    public ProtocolBaseModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.protocolID);
        dest.writeInt(this.callbackId);
        dest.writeInt(this.protocolVersion);
        dest.writeString(this.packageName);
        dest.writeBundle(this.extraData);
        dest.writeLong(this.timeout);
    }

    protected ProtocolBaseModel(Parcel in) {
        this.protocolID = in.readInt();
        this.callbackId = in.readInt();
        this.protocolVersion = in.readInt();
        this.packageName = in.readString();
        this.extraData = in.readBundle();
        this.timeout = in.readLong();
    }

}

