package com.xinshang.audient.model.entities;

import com.google.gson.annotations.SerializedName;

public class Version {
    @SerializedName("version")
    public int version;
    @SerializedName("versionName")
    public String versionName;
    @SerializedName("url")
    public String url;
}
