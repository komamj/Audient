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
package com.koma.audient.model.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.koma.audient.model.entities.Audient;

import java.util.List;

@Dao
public interface AudientDao {
    @Query("SELECT * FROM audient")
    List<Audient> getAllAudients();

    @Query("SELECT * FROM audient WHERE content_id = (:contentId)")
    Audient findById(long contentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAudients(Audient... audients);

    @Delete
    int deleteAudients(Audient... audients);
}
