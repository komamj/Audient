package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koma_20 on 2018/3/5.
 */

public class Location {
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
}
