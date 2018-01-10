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
package com.koma.audient.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MusicFileItem implements Parcelable {
    @SerializedName("id")
    public int contentId;
    @SerializedName("musicname")
    public String musicName;
    @SerializedName("actorname")
    public String actorName;

    protected MusicFileItem(Parcel in) {
        contentId = in.readInt();
        musicName = in.readString();
        actorName = in.readString();
    }

    public static final Creator<MusicFileItem> CREATOR = new Creator<MusicFileItem>() {
        @Override
        public MusicFileItem createFromParcel(Parcel in) {
            return new MusicFileItem(in);
        }

        @Override
        public MusicFileItem[] newArray(int size) {
            return new MusicFileItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(contentId);
        parcel.writeString(musicName);
        parcel.writeString(actorName);
    }
}
