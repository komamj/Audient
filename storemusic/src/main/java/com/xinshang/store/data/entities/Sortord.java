package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koma_20 on 2018/3/10.
 */

public class Sortord {
    @SerializedName("direction")
    public String direction;
    @SerializedName("property")
    public String property;
    @SerializedName("ignoreCase")
    public boolean ignoreCase;
    @SerializedName("nullHandling")
    public String nullHandling;
    @SerializedName("descending")
    public boolean descending;
    @SerializedName("ascending")
    public boolean ascending;
}
