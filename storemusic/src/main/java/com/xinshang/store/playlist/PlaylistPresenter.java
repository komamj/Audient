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
import com.xinshang.store.BuildConfig;
import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.CommandRequest;
import com.xinshang.store.data.entities.CommandResponse;
import com.xinshang.store.data.entities.MessageEvent;
import com.xinshang.store.data.entities.StoreSong;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Publisher;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private static final String REBOOT = "reboot";
    private static final String COMMAND_PLAY_MODE = "playmod";
    private static final String COMMAND_PLAY_STATE = "playstate";

    private PlaylistContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private final OkHttpClient mClient;

    private WebSocket mWebSocket;

    private boolean mIsPlaying;

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

        EventBus.getDefault().register(this);

        Request request = new Request.Builder()
                .url(BuildConfig.WEBSOCKET_ENDPOINT)
                .build();

        mWebSocket = mClient.newWebSocket(request, this);

        loadStorePlaylist();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        LogUtils.i(TAG, "onMessageEvent");

        if (TextUtils.equals(messageEvent.getMessage(), Constants.MESSAGE_PLAYLIST_CHANGED)) {
            loadStorePlaylist();
        }
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
        LogUtils.i(TAG, "onMessage text : " + text);

        final Disposable disposable = mRepository.parsingCommandResponse(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CommandResponse<String>>() {
                    @Override
                    public void onNext(CommandResponse<String> commandResponse) {
                        if (TextUtils.equals(COMMAND_STATUS, commandResponse.action)
                                && commandResponse.code == 0) {
                            if (mView.isActive()) {
                                mView.setNextActive(true);
                                mView.setPauseActive(true);
                                mView.setStopActive(true);
                            }
                            String message = commandResponse.data;
                            if (TextUtils.equals(message, STOPPED)) {
                                mIsPlaying = false;
                                if (mView.isActive()) {
                                    mView.setNextActive(false);
                                }
                            } else if (TextUtils.equals(message, PAUSED)) {
                                mIsPlaying = false;
                                if (mView.isActive()) {
                                    mView.setNextActive(true);
                                }
                            } else if (TextUtils.equals(message, PLAYING)) {
                                mIsPlaying = true;
                                if (mView.isActive()) {
                                    mView.setNextActive(true);
                                }
                                loadNowPlaying(commandResponse.message);
                            }
                        } else if (TextUtils.equals(COMMAND_PLAY, commandResponse.action)
                                && commandResponse.code == 0) {
                            mIsPlaying = true;

                            if (mView.isActive()) {
                                mView.setNextActive(true);
                                mView.setPauseActive(true);
                                mView.setStopActive(true);
                            }

                            loadNowPlaying(commandResponse.message);
                        } else if (TextUtils.equals(COMMAND_START, commandResponse.action)
                                && commandResponse.code == 0) {
                            mIsPlaying = true;
                        } else if (TextUtils.equals(COMMAND_PAUSE, commandResponse.action)
                                && commandResponse.code == 0) {
                            mIsPlaying = false;
                        } else if (TextUtils.equals(COMMAND_STOP, commandResponse.action)
                                && commandResponse.code == 0) {
                            mIsPlaying = false;

                            if (mView.isActive()) {
                                mView.setNextActive(false);
                            }
                        } else if (TextUtils.equals(COMMAND_PLAY_STATE, commandResponse.action)
                                && commandResponse.code == 0) {
                            if (mView.isActive()) {
                                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                                try {
                                    Date date = format.parse(commandResponse.data);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int second = calendar.get(Calendar.SECOND)
                                            + calendar.get(Calendar.MINUTE) * 60
                                            + calendar.get(Calendar.HOUR) * 60 * 60;
                                    mView.updateProgress(second);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
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

        EventBus.getDefault().unregister(this);

        mWebSocket.cancel();

        mDisposables.clear();
    }

    @Override
    public void loadNowPlaying(String id) {
        Disposable disposable = Flowable.just(id)
                .flatMap(new Function<String, Publisher<List<StoreSong>>>() {
                    @Override
                    public Publisher<List<StoreSong>> apply(String s) throws Exception {
                        List<StoreSong> storeSongs = new ArrayList<>();
                        for (StoreSong storeSong : mPlaylist) {
                            StoreSong playlist = new StoreSong();
                            playlist.id = storeSong.id;
                            playlist.mediaName = storeSong.mediaName;
                            playlist.mediaId = storeSong.mediaId;
                            playlist.storeId = storeSong.storeId;
                            playlist.albumId = storeSong.albumId;
                            playlist.albumName = storeSong.albumName;
                            playlist.artistId = storeSong.artistId;
                            playlist.artistName = storeSong.artistName;
                            playlist.demandId = storeSong.demandId;
                            playlist.demandTime = storeSong.demandTime;
                            playlist.mediaInterval = storeSong.mediaInterval;
                            playlist.mediaSource = storeSong.mediaSource;
                            playlist.joinedDate = storeSong.joinedDate;
                            playlist.userId = storeSong.userId;

                            if (TextUtils.equals(s, storeSong.id)) {
                                playlist.isPlaying = true;

                                mNowPlaying = storeSong;
                            } else {
                                playlist.isPlaying = false;
                            }

                            storeSongs.add(playlist);
                        }
                        return Flowable.just(storeSongs);
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
        Disposable disposable = mRepository.getStorePlaylist(storeId)
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
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
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
        } else {
            sendCommand(COMMAND_START);
        }
    }

    @Override
    public void stop() {
        sendCommand(COMMAND_STOP);
    }

    @Override
    public void next() {
        sendCommand(COMMAND_NEXT);
    }

    @Override
    public void shuffle(String mode) {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.action = COMMAND_PLAY_MODE;
        commandRequest.store = mRepository.getStoreId();
        commandRequest.data = mode;
        String message = new Gson().toJson(commandRequest);
        mWebSocket.send(message);
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
    public boolean isPlaying() {
        return mIsPlaying;
    }

    @Override
    public void play(String id) {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.action = COMMAND_PLAY;
        commandRequest.store = mRepository.getStoreId();
        commandRequest.data = id;
        String message = new Gson().toJson(commandRequest);

        mWebSocket.send(message);
    }

    @Override
    public void deleteStoreSong(StoreSong storeSong, String reason) {
        String id = storeSong.id;

        mRepository.removeSongFromPlaylist(id, reason)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.resultCode == 0) {
                            LogUtils.i(TAG, "deleteStoreSong completed");
                            unSubscribe();
                            subscribe();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "deleteStoreSong error : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void reboot() {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.action = REBOOT;
        commandRequest.store = mRepository.getStoreId();
        String message = new Gson().toJson(commandRequest);

        mWebSocket.send(message);
    }
}
