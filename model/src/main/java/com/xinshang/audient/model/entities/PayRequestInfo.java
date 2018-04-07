package com.xinshang.audient.model.entities;

import com.google.gson.annotations.SerializedName;

public class PayRequestInfo {
    @SerializedName("appid")
    public String appId;
    @SerializedName("partnerid")
    public String partnerId;
    @SerializedName("prepayid")
    public String prepayId;
    @SerializedName("noncestr")
    public String nonceStr;
    @SerializedName("timestamp")
    public String timeStamp;
    @SerializedName("package")
    public String packageValue;
    @SerializedName("sign")
    public String sign;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PayRequestInfo has appId ");
        builder.append(appId);
        builder.append(",partnerId ");
        builder.append(partnerId);
        builder.append(",prepayId ");
        builder.append(prepayId);
        builder.append(", nonceStr ");
        builder.append(nonceStr);
        builder.append(",timeStamp ");
        builder.append(timeStamp);
        builder.append(",packageValue ");
        builder.append(packageValue);
        builder.append(",sign ");
        builder.append(sign);

        return builder.toString();
    }
}
