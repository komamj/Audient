package com.koma.audient.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopSong extends Audient {
    @SerializedName("QueryContentBillboardResponse")
    public QueryContentBillboardResponse queryContentBillboardResponse;

    public static class QueryContentBillboardResponse {
        @SerializedName("page")
        public int page;
        @SerializedName("count")
        public int count;
        @SerializedName("total")
        public int total;
        @SerializedName("musicItemList")
        public MusicItemList musicItemList;
    }

    public static class MusicItemList {
        @SerializedName("musicItem")
        public List<Music> musics;
    }
}
