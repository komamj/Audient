package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlayAllRequest {
    @SerializedName("items")
    public List<TencentMusic> tencentMusics;
}
