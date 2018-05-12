package com.xinshang.audient.model.entities;

import com.google.gson.annotations.SerializedName;

public class Coupon {
    public String id;
    public String userId;
    public String cuponId;
    public String name;
    public Object desc;
    public Object startDate;
    public Object expiryDate;
    @SerializedName("used")
    public boolean used;
}
