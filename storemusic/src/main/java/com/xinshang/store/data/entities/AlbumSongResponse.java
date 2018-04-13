package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlbumSongResponse {
    @SerializedName("name")
    public String name;
    @SerializedName("items")
    public List<Song> songs;
}
