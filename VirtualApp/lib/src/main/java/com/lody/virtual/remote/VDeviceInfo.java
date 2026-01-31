package com.lody.virtual.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.lody.virtual.os.VEnvironment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Lody
 */
public class VDeviceInfo implements Parcelable {

    public String deviceId;
    public String androidId;
    public String wifiMac;
    public String bluetoothMac;
    public String iccId;
    public String serial;
    public String gmsAdId;

    public String product;
    public String manufacturer;
    public String brand;
    public String model;
    public String device;
    public String bootloader;
    public String hardware;
    public String id;
    public String display;
    public String fingerprint;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceId);
        dest.writeString(this.androidId);
        dest.writeString(this.wifiMac);
        dest.writeString(this.bluetoothMac);
        dest.writeString(this.iccId);
        dest.writeString(this.serial);
        dest.writeString(this.gmsAdId);

        dest.writeString(this.product);
        dest.writeString(this.manufacturer);
        dest.writeString(this.brand);
        dest.writeString(this.model);
        dest.writeString(this.device);
        dest.writeString(this.bootloader);
        dest.writeString(this.hardware);
        dest.writeString(this.id);
        dest.writeString(this.display);
        dest.writeString(this.fingerprint);
    }

    public VDeviceInfo() {}

    public VDeviceInfo(Parcel in) {
        this.deviceId = in.readString();
        this.androidId = in.readString();
        this.wifiMac = in.readString();
        this.bluetoothMac = in.readString();
        this.iccId = in.readString();
        this.serial = in.readString();
        this.gmsAdId = in.readString();

        this.product = in.readString();
        this.manufacturer = in.readString();
        this.brand = in.readString();
        this.model = in.readString();
        this.device = in.readString();
        this.bootloader = in.readString();
        this.hardware = in.readString();
        this.id = in.readString();
        this.display = in.readString();
        this.fingerprint = in.readString();
    }

    public static final Parcelable.Creator<VDeviceInfo> CREATOR = new Parcelable.Creator<VDeviceInfo>() {
        @Override
        public VDeviceInfo createFromParcel(Parcel source) {
            return new VDeviceInfo(source);
        }

        @Override
        public VDeviceInfo[] newArray(int size) {
            return new VDeviceInfo[size];
        }
    };

    public File getWifiFile(int userId) {
        File wifiMacFie = VEnvironment.getWifiMacFile(userId);
        if (!wifiMacFie.exists()) {
            try {
                RandomAccessFile file = new RandomAccessFile(wifiMacFie, "rws");
                file.write((wifiMac + "\n").getBytes());
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wifiMacFie;
    }
}
