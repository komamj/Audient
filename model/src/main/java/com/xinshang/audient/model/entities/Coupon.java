package com.xinshang.audient.model.entities;

import com.google.gson.annotations.SerializedName;

public class Coupon {
    @SerializedName("id")
    public String id;
    @SerializedName("userId")
    public String userId;
    @SerializedName("cuponId")
    public String cuponId;
    @SerializedName("name")
    public String name;
    @SerializedName("used")
    public boolean used;
}
