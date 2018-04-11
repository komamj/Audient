package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

public class Playlist {
    @SerializedName("dissid")
    public String id;
    @SerializedName("diss_status")
    public int status;
    @SerializedName("createtime")
    public String createTime;
    @SerializedName("creator")
    public Creator creator;
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
}
