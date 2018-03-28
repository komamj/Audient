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

import java.util.List;

/**
 * Created by koma on 3/5/18.
 */

public class CommentDataBean {
    @SerializedName("inStore")
    public InStoreBean inStoreComment;
    @SerializedName("others")
    public OthersBean othersComment;

    public static class InStoreBean {
        @SerializedName("last")
        public boolean last;
        @SerializedName("totalElements")
        public int totalElements;
        @SerializedName("totalPages")
        public int totalPages;
        @SerializedName("number")
        public int number;
        @SerializedName("size")
        public int size;
        @SerializedName("sort")
        public List<Sortord> sortords;
        @SerializedName("numberOfElements")
        public int numberOfElements;
        @SerializedName("first")
        public boolean first;
        @SerializedName("content")
        public List<Comment> comments;
    }

    public static class OthersBean {
        @SerializedName("last")
        public boolean last;
        @SerializedName("totalElements")
        public int totalElements;
        @SerializedName("totalPages")
        public int totalPages;
        @SerializedName("number")
        public int number;
        @SerializedName("size")
        public int size;
        @SerializedName("sort")
        public List<Sortord> sortords;
        @SerializedName("numberOfElements")
        public int numberOfElements;
        @SerializedName("first")
        public boolean first;
        @SerializedName("content")
        public List<Comment> comments;
    }
}
