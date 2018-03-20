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
package com.xinshang.store.playlist;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.CommandRequest;
import com.xinshang.store.data.entities.CommandResponse;
import com.xinshang.store.data.entities.StorePlaylist;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
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
    private static final String STOPPED = "stoped";
    private static final String PAUSED = "paused";

    private PlaylistContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private final OkHttpClient mClient;

    private WebSocket mWebSocket;

    private boolean mIsPlaying;

    private String mNowPlayingId = "";

    private List<StorePlaylist> mPlaylist;

    @Inject
    public PlaylistPresenter(PlaylistContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();

        mClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    @Inject
    void setUpListener() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        LogUtils.i(TAG, "subscribe");

        loadStorePlaylist();

        Request request = new Request.Builder()
                .url(Constants.PLAYLIST_STATUS_HOST)
                .build();

        mWebSocket = mClient.newWebSocket(request, this);
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
                .filter(new Predicate<CommandResponse<String>>() {
                    @Override
                    public boolean test(CommandResponse<String> commandResponse) throws Exception {
                        return TextUtils.equals(commandResponse.store, mRepository.getStoreId());
                    }
                })
                .doOnNext(new Consumer<CommandResponse<String>>() {
                    @Override
                    public void accept(CommandResponse<String> commandResponse) throws Exception {
                        if (TextUtils.equals(COMMAND_STATUS, commandResponse.action)
                                && commandResponse.code == 0) {
                            String message = commandResponse.data;
                            if (TextUtils.equals(message, STOPPED) || TextUtils.equals(message, PAUSED)) {
                                mIsPlaying = false;
                            } else {
                                mIsPlaying = true;
                                String id = commandResponse.message;
                                if (!TextUtils.equals(mNowPlayingId, id)) {
                                    mNowPlayingId = id;
                                    loadNowPlaying(id);
                                }
                            }
                        } else if (TextUtils.equals(COMMAND_PLAY, commandResponse.action)
                                && commandResponse.code == 0) {
                            mIsPlaying = true;

                            String message = commandResponse.message;
                            if (!TextUtils.equals(mNowPlayingId, message)) {
                                mNowPlayingId = message;
                                loadNowPlaying(message);
                            }
                        } else if (TextUtils.equals(COMMAND_START, commandResponse.action)
                                && commandResponse.code == 0) {
                            mIsPlaying = true;
                        } else if (TextUtils.equals(COMMAND_PAUSE, commandResponse.action)
                                && commandResponse.code == 0) {
                            mIsPlaying = false;
                        } else if (TextUtils.equals(COMMAND_STOP, commandResponse.action)
                                && commandResponse.code == 0) {
                            mIsPlaying = false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CommandResponse<String>>() {
                    @Override
                    public void onNext(CommandResponse<String> commandResponse) {
                        LogUtils.i(TAG, "onMessage text : " + text);
                        if (mView.isActive()) {
                            mView.updatePlayIcon(isPlaying());
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
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mWebSocket.cancel();

        mDisposables.clear();
    }

    @Override
    public void loadNowPlaying(String id) {
        Flowable.just(id)
                .map(new Function<String, StorePlaylist>() {
                    @Override
                    public StorePlaylist apply(String s) throws Exception {
                        for (StorePlaylist storePlaylist : mPlaylist) {
                            if (TextUtils.equals(s, storePlaylist.id)) {
                                return storePlaylist;
                            }
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<StorePlaylist>() {
                    @Override
                    public void onNext(StorePlaylist storePlaylist) {
                        if (mView.isActive()) {
                            mView.showNowPlaying(storePlaylist);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadStorePlaylist() {
        String storeId = mRepository.getStoreId();
        Disposable disposable = mRepository.getStorePlaylist(storeId)
                .map(new Function<ApiResponse<List<StorePlaylist>>, List<StorePlaylist>>() {
                    @Override
                    public List<StorePlaylist> apply(ApiResponse<List<StorePlaylist>> listApiResponse) throws Exception {
                        return listApiResponse.data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<StorePlaylist>>() {
                    @Override
                    public void onNext(List<StorePlaylist> storePlaylists) {
                        mPlaylist = storePlaylists;

                        if (mView.isActive()) {
                            mView.showPlaylist(storePlaylists);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadStorePlaylist error : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.showProgressBar(false);
                        }

                        sendCommand(COMMAND_BIND);
                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void doPauseOrPlay() {
        if (isPlaying()) {
            sendCommand(COMMAND_PAUSE);
            mIsPlaying = false;
        } else {
            sendCommand(COMMAND_START);
            mIsPlaying = true;
        }

        if (mView.isActive()) {
            mView.updatePlayIcon(isPlaying());
        }
    }

    @Override
    public void stop() {
        sendCommand(COMMAND_STOP);
        mIsPlaying = false;
        if (mView.isActive()) {
            mView.updatePlayIcon(isPlaying());
        }
    }

    @Override
    public void next() {
        sendCommand(COMMAND_NEXT);
    }

    @Override
    public void thumbUpSong(TencentMusic audient) {
        LogUtils.i(TAG, "thumbUp");
    }

    @Override
    public void sendCommand(String command) {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.action = command;
        commandRequest.store = mRepository.getStoreId();
        String message = new Gson().toJson(commandRequest);

        LogUtils.i(TAG, "senCommand : " + message);

        mWebSocket.send(message);
    }

    @Override
    public boolean isPlaying() {
        return mIsPlaying;
    }

    @Override
    public void onCommandResponse(String message) {

    }
}
