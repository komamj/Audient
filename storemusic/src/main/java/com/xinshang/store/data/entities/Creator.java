package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

public class Creator {
    @SerializedName("avatarUrl")
    public String avatarUrl;
    @SerializedName("creator_uin")
    public String creator_uin;
    public String encrypt_uin;
    public int followflag;
    public int isVip;
    public String name;
    public long qq;
    public int singerid;
    public String singermid;
    public int type;
}
