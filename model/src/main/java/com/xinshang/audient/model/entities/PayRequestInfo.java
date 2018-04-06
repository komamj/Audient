package com.xinshang.audient.model.entities;

import com.google.gson.annotations.SerializedName;

public class PayRequestInfo {
    @SerializedName("appid")
    public String appId;
    @SerializedName("partnerid")
    public String partnerId;
    @SerializedName("prepareid")
    public String prepareId;
    @SerializedName("noncestr")
    public String nonceStr;
    @SerializedName("timestamp")
    public String timeStamp;
    @SerializedName("package")
    public String packageValue;
    @SerializedName("sign")
    public String sign;
}
