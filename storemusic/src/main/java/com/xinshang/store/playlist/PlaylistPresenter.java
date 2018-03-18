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
import com.xinshang.store.data.entities.NowPlayingResponse;
import com.xinshang.store.data.entities.StorePlaylist;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private static final String COMMAND_NEXT = "next";
    private static final String COMMAND_STOP = "stop";
    private static final String COMMAND_PAUSE = "pause";
    private static final String COMMAND_PLAY = "play";

    private PlaylistContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private final OkHttpClient mClient;

    private WebSocket mWebSocket;

    private boolean mIsPlaying;

    private String mMessage;

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

        sendCommand(COMMAND_BIND);

        Flowable.just("").delay(3, TimeUnit.SECONDS)
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(String s) throws Exception {
                        return Flowable.just(mMessage);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<String>() {
                    @Override
                    public void onNext(String s) {
                        if (TextUtils.isEmpty(s)) {
                            mIsPlaying = false;
                        } else {
                            mIsPlaying = true;
                        }
                        if (mView.isActive()) {
                            mView.updatePlayIcon(mIsPlaying);
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

    /**
     * Invoked when a text (type {@code 0x1}) message has been received.
     */
    @Override
    public void onMessage(WebSocket webSocket, final String text) {
        mMessage = text;

        LogUtils.i(TAG, "onMessage string : " + text);

        Disposable disposable = mRepository.parsingCommandResponse(text)
                .filter(new Predicate<CommandResponse>() {
                    @Override
                    public boolean test(CommandResponse commandResponse) throws Exception {
                        return TextUtils.equals(commandResponse.store, mRepository.getStoreId());
                    }
                })
                .doOnNext(new Consumer<CommandResponse>() {
                    @Override
                    public void accept(CommandResponse commandResponse) throws Exception {
                        LogUtils.i(TAG, "onMessage string : " + text);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CommandResponse>() {
                    @Override
                    public void onNext(CommandResponse commandResponse) {
                        LogUtils.i(TAG, "onMessage commandResponse : " + commandResponse.toString());
                        if (TextUtils.equals(COMMAND_PLAY, commandResponse.action)
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
    public void loadNowPlaying() {
        String storeId = mRepository.getStoreId();
        Disposable disposable = mRepository.getNowPlaying(storeId)
                .map(new Function<NowPlayingResponse, TencentMusic>() {
                    @Override
                    public TencentMusic apply(NowPlayingResponse nowPlayingResponse) throws Exception {
                        return nowPlayingResponse.tencentMusic;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<TencentMusic>() {
                    @Override
                    public void onNext(TencentMusic tencentMusic) {
                        if (mView.isActive()) {
                            mView.showNowPlaying(tencentMusic);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

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
            sendCommand(COMMAND_PLAY);
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
        if (isPlaying()) {
            sendCommand(COMMAND_NEXT);
        } else {
            sendCommand(COMMAND_PLAY);
        }

        mIsPlaying = true;

        if (mView.isActive()) {
            mView.updatePlayIcon(isPlaying());
        }
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
}
