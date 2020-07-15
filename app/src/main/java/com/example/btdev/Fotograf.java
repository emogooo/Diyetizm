package com.example.btdev;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Fotograf implements Parcelable {
    private Uri uri;
    private String url;

    public Fotograf(Uri uri) {
        this.uri = uri;
    }

    public Fotograf(String url) {
        this.url = url;
    }

    protected Fotograf(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        url = in.readString();
    }

    public static final Creator<Fotograf> CREATOR = new Creator<Fotograf>() {
        @Override
        public Fotograf createFromParcel(Parcel in) {
            return new Fotograf(in);
        }

        @Override
        public Fotograf[] newArray(int size) {
            return new Fotograf[size];
        }
    };

    public Uri getUri() {
        return uri;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
        dest.writeString(url);
    }
}
