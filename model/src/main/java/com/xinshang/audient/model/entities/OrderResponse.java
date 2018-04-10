package com.xinshang.audient.model.entities;

import com.google.gson.annotations.SerializedName;

public class OrderResponse {
    @SerializedName("order")
    public Order order;
    @SerializedName("prepay")
    public PayRequestInfo payRequestInfo;
}
