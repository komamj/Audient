package com.xinshang.store.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Playlist implements Parcelable {
    @SerializedName("dissid")
    public String id;
    @SerializedName("diss_status")
    public int status;
    @SerializedName("createtime")
    public String createTime;
    @SerializedName("dissname")
    public String name;
    @SerializedName("imgurl")
    public String imageUrl;
    @SerializedName("introduction")
    public String description;
    @SerializedName("listennum")
    public int listenNum;
    @SerializedName("song_count")
    public int songCount;

    protected Playlist(Parcel in) {
        id = in.readString();
        status = in.readInt();
        createTime = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        description = in.readString();
        listenNum = in.readInt();
        songCount = in.readInt();
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(status);
        parcel.writeString(createTime);
        parcel.writeString(name);
        parcel.writeString(imageUrl);
        parcel.writeString(description);
        parcel.writeInt(listenNum);
        parcel.writeInt(songCount);
    }
}
