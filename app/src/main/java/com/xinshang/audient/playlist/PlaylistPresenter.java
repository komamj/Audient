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
package com.xinshang.audient.playlist;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.CommandRequest;
import com.xinshang.audient.model.entities.CommandResponse;
import com.xinshang.audient.model.entities.StoreSong;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import org.reactivestreams.Publisher;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;

public class PlaylistPresenter extends WebSocketListener implements PlaylistContract.Presenter {
    private static final String TAG = PlaylistPresenter.class.getSimpleName();

    private static final String COMMAND_BIND = "bind";
    private static final String COMMAND_STATUS = "status";
    private static final String COMMAND_NEXT = "next";
    private static final String COMMAND_STOP = "stop";
    private static final String COMMAND_PAUSE = "pause";
    private static final String COMMAND_PLAY = "play";
    private static final String COMMAND_START = "start";
    private static final String PLAYING = "playing";
    private static final String FINISHED = "finished";
    private static final String STOPPED = "stoped";
    private static final String PAUSED = "paused";

    private PlaylistContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private final OkHttpClient mClient;

    private WebSocket mWebSocket;

    private List<StoreSong> mPlaylist;

    private StoreSong mNowPlaying;

    @Inject
    public PlaylistPresenter(PlaylistContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();

        mClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Inject
    void setUpListener() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        LogUtils.i(TAG, "subscribe");

        Request request = new Request.Builder()
                .url(Constants.PLAYLIST_STATUS_HOST)
                .build();

        mWebSocket = mClient.newWebSocket(request, this);

        loadStorePlaylist();
    }

    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        LogUtils.i(TAG, "onOpen");
    }

    /**
     * Invoked when a text (type {@code 0x1}) message has been received.
     */
    @Override
    public void onMessage(WebSocket webSocket, final String text) {
        final Disposable disposable = mRepository.parsingCommandResponse(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CommandResponse<String>>() {
                    @Override
                    public void onNext(CommandResponse<String> commandResponse) {
                        LogUtils.i(TAG, "onMessage text : " + commandResponse.toString());
                        if (TextUtils.equals(COMMAND_STATUS, commandResponse.action)
                                && commandResponse.code == 0) {
                            String message = commandResponse.data;
                            if (TextUtils.equals(message, STOPPED)) {
                                if (mView.isActive()) {
                                    mView.setPlayingIndicator(false);
                                }
                            } else if (TextUtils.equals(message, PAUSED)) {
                                if (mView.isActive()) {
                                    mView.setPlayingIndicator(false);
                                }
                            } else if (TextUtils.equals(message, PLAYING)) {
                                if (mView.isActive()) {
                                    mView.setPlayingIndicator(true);
                                }
                                loadNowPlaying(commandResponse.message);
                            }
                        } else if (TextUtils.equals(COMMAND_PLAY, commandResponse.action)
                                && commandResponse.code == 0) {
                            if (mView.isActive()) {
                                mView.setPlayingIndicator(true);
                            }
                            loadNowPlaying(commandResponse.message);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "onMessage error :" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }

    /**
     * Invoked when a binary (type {@code 0x2}) message has been received.
     */
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        LogUtils.i(TAG, "onMessage bytestring : " + bytes.toString());
    }

    /**
     * Invoked when the peer has indicated that no more incoming messages will be transmitted.
     */
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        LogUtils.i(TAG, "onClosing code : " + code + ",reason :" + reason);
    }

    /**
     * Invoked when both peers have indicated that no more messages will be transmitted and the
     * connection has been successfully released. No further calls to this listener will be made.
     */
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        LogUtils.i(TAG, "onClosed code : " + code + ",reason :" + reason);
    }

    /**
     * Invoked when a web socket has been closed due to an error reading from or writing to the
     * network. Both outgoing and incoming messages may have been lost. No further calls to this
     * listener will be made.
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        LogUtils.i(TAG, "onFailure " + t.getMessage());
        if (response != null) {
            LogUtils.e(TAG, "onFailure response : " + response.message());
        }

        if (t instanceof SocketTimeoutException) {
            unSubscribe();
            subscribe();
        }
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mWebSocket.cancel();

        mDisposables.clear();
    }

    @Override
    public void loadNowPlaying(String id) {
        Disposable disposable = Flowable.just(id)
                .flatMap(new Function<String, Publisher<List<StoreSong>>>() {
                    @Override
                    public Publisher<List<StoreSong>> apply(String s) {
                        List<StoreSong> storePlaylists = new ArrayList<>();
                        for (StoreSong storePlaylist : mPlaylist) {
                            StoreSong playlist = new StoreSong();
                            playlist.id = storePlaylist.id;
                            playlist.mediaName = storePlaylist.mediaName;
                            playlist.mediaId = storePlaylist.mediaId;
                            playlist.storeId = storePlaylist.storeId;
                            playlist.albumId = storePlaylist.albumId;
                            playlist.albumName = storePlaylist.albumName;
                            playlist.artistId = storePlaylist.artistId;
                            playlist.artistName = storePlaylist.artistName;
                            playlist.demandId = storePlaylist.demandId;
                            playlist.demandTime = storePlaylist.demandTime;
                            playlist.mediaInterval = storePlaylist.mediaInterval;
                            playlist.mediaSource = storePlaylist.mediaSource;
                            playlist.joinedDate = storePlaylist.joinedDate;
                            playlist.userId = storePlaylist.userId;

                            if (TextUtils.equals(s, storePlaylist.id)) {
                                playlist.isPlaying = true;

                                mNowPlaying = storePlaylist;
                            } else {
                                playlist.isPlaying = false;
                            }

                            storePlaylists.add(playlist);
                        }
                        return Flowable.just(storePlaylists);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<StoreSong>>() {
                    @Override
                    public void onNext(List<StoreSong> storePlaylists) {
                        if (mView.isActive()) {
                            mView.showNowPlaying(mNowPlaying);
                            mView.showPlaylist(storePlaylists);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadNowPlaying error : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void loadStorePlaylist() {
        String storeId = mRepository.getStoreId();

        mRepository.getStorePlaylist(storeId)
                .map(new Function<ApiResponse<List<StoreSong>>, List<StoreSong>>() {
                    @Override
                    public List<StoreSong> apply(ApiResponse<List<StoreSong>> listApiResponse) {
                        return listApiResponse.data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<StoreSong>>() {
                    @Override
                    public void onNext(List<StoreSong> storePlaylists) {
                        mPlaylist = storePlaylists;
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadStorePlaylist error : " + t.getMessage());

                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }

                        sendCommand(COMMAND_BIND);
                    }
                });
    }

    @Override
    public void thumbUpSong(Audient audient) {
        LogUtils.i(TAG, "thumbUp");
    }

    @Override
    public void sendCommand(String command) {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.action = command;
        commandRequest.store = mRepository.getStoreId();
        String message = new Gson().toJson(commandRequest);

        mWebSocket.send(message);
    }

    @Override
    public void onCommandResponse(String message) {

    }
}
