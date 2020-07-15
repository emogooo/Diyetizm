package com.example.btdev;

import android.os.Parcel;
import android.os.Parcelable;

public class Ogun implements Parcelable {
    int fotograf;
    String isim;

    public Ogun(int fotograf, String isim) {
        this.fotograf = fotograf;
        this.isim = isim;
    }

    protected Ogun(Parcel in) {
        fotograf = in.readInt();
        isim = in.readString();
    }

    public static final Creator<Ogun> CREATOR = new Creator<Ogun>() {
        @Override
        public Ogun createFromParcel(Parcel in) {
            return new Ogun(in);
        }

        @Override
        public Ogun[] newArray(int size) {
            return new Ogun[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fotograf);
        dest.writeString(isim);
    }
}
