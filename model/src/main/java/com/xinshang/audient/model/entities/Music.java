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

public class Music {
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("mediaId")
    public String mediaId;
    @SerializedName("mediaName")
    public String mediaName;
    @SerializedName("mediaInterval")
    public String mediaInterval;
    @SerializedName("artistId")
    public String artistId;
    @SerializedName("artistName")
    public String artistName;
    @SerializedName("albumId")
    public String albumId;
    @SerializedName("albumName")
    public String albumName;
    @SerializedName("price")
    public int price;
    @SerializedName("discount")
    public int discount;
    @SerializedName("discountPrice")
    public int discountPrice;
    @SerializedName("paymentWay")
    public String paymentWay;
    @SerializedName("free")
    public boolean free;
    @SerializedName("freeWay")
    public int freeWay;
}
