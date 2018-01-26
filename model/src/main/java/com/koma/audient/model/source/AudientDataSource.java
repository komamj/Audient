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

import com.koma.audient.model.entities.AudientTest;
import com.koma.audient.model.entities.Comment;
import com.koma.audient.model.entities.FileResult;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.SongDetailResult;
import com.koma.audient.model.entities.TopListResult;
import com.koma.audient.model.entities.ToplistDetailResult;

import java.util.List;

import io.reactivex.Flowable;

public interface AudientDataSource {
    Flowable<List<AudientTest>> getAudientTests();

    Flowable<List<TopListResult>> getTopLists();

    Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime);

    Flowable<SearchResult> getSearchReults(String keyword);

    Flowable<LyricResult> getLyric(String id);

    Flowable<SongDetailResult> getSongDetailResult(String id);

    Flowable<FileResult> getFileResult(String id);

    Flowable<List<Comment>> getComments(long id);
}
