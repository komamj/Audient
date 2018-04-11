package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistResponse {
    @SerializedName("display_num")
    public int displayNum;
    @SerializedName("list")
    public List<Playlist> playlists;
}
