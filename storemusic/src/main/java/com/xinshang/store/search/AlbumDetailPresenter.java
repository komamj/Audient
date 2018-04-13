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
package com.xinshang.store.search;

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.AlbumResponse;
import com.xinshang.store.data.entities.Song;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by koma on 4/13/18.
 */

public class AlbumDetailPresenter implements AlbumDetailContract.Presenter {
    private static final String TAG = AlbumDetailPresenter.class.getSimpleName();

    private final AlbumDetailContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    private List<Song> mSongs;

    @Inject
    public AlbumDetailPresenter(AlbumDetailContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }

    @Override
    public void loadAlbumSongs(AlbumResponse.Album album) {

    }

    @Override
    public void addToPlaylist(Song tencentMusic) {

    }

    @Override
    public void playAll() {

    }
}
