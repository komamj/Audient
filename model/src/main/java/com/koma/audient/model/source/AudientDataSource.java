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
package com.koma.audient.model.source;

import android.support.annotation.NonNull;

import com.koma.audient.model.entities.Album;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.Lyric;
import com.koma.audient.model.entities.MusicFileItem;
import com.koma.audient.model.entities.TopList;

import java.util.List;

import io.reactivex.Flowable;

public interface AudientDataSource {
    Flowable<List<Audient>> getAudients();

    Flowable<List<TopList.BillboardListResponse.Billboard>> getTopLists();

    Flowable<List<MusicFileItem>> getTopSongs(@NonNull String billboardId, int count, int page);

    Flowable<List<MusicFileItem>> getSearchReults(String keyword, String musicType, int count, int page);

    Flowable<Lyric> getLyric(String id, String idType, String musicName, String actorName,
                             String type);

    Flowable<Album> getAlbum(MusicFileItem musicFileItem);
}
