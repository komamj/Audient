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
package com.xinshang.store.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.xinshang.store.data.entities.TencentMusic;

import java.util.List;

@Dao
public interface AudientDao {
    @Query("SELECT * FROM music")
    List<TencentMusic> getAllMusics();

    @Query("SELECT * FROM music WHERE mediaId = (:id)")
    TencentMusic findById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMusics(TencentMusic... musics);

    @Delete
    int deleteMusics(TencentMusic... musics);
}
