package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistSongResponse {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("coverImage")
    public String coverImage;
    @SerializedName("description")
    public String description;
    @SerializedName("items")
    public List<Song> tencentMusics;
}
