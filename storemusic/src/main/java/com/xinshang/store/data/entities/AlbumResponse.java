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
package com.xinshang.store.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by koma on 4/13/18.
 */

public class AlbumResponse {
    public static class Album implements Parcelable {
        protected Album(Parcel in) {
        }

        public static final Creator<Album> CREATOR = new Creator<Album>() {
            @Override
            public Album createFromParcel(Parcel in) {
                return new Album(in);
            }

            @Override
            public Album[] newArray(int size) {
                return new Album[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
        }
    }
}
