/*
 * Copyright 2017 Koma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinshang.audient.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koma on 3/5/18.
 */

public class Comment {
    @SerializedName("id")
    public String id;
    @SerializedName("username")
    public String userName;
    @SerializedName("nickname")
    public String userNickname;
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("mediaSource")
    public int mediaSource;
    @SerializedName("mediaId")
    public String mediaId;
    @SerializedName("comment")
    public String comment;
    @SerializedName("parentId")
    public String parentId;
    @SerializedName("voteCount")
    public int voteCount;
    @SerializedName("voted")
    public boolean voted;
    @SerializedName("commentDate")
    public String commentDate;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("storeName")
    public String storeName;
    @SerializedName("storeAddress")
    public String storeAddress;
}
