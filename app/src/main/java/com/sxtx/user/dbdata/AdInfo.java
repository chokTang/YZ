package com.sxtx.user.dbdata;

import android.os.Parcel;
import android.os.Parcelable;

public class AdInfo implements Parcelable {
    private String url;
    private String img;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AdInfo() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public AdInfo(Parcel in) {
        url = in.readString();
        img = in.readString();
        id = in.readLong();
    }

    public static final Creator<AdInfo> CREATOR = new Creator<AdInfo>() {
        @Override
        public AdInfo createFromParcel(Parcel in) {
            return new AdInfo(in);
        }

        @Override
        public AdInfo[] newArray(int size) {
            return new AdInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(img);
        parcel.writeLong(id);
    }
}
