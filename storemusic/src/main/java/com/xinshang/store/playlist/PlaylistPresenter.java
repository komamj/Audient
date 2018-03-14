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

import com.google.gson.Gson;
import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.CommandRequest;
import com.xinshang.store.data.entities.NowPlayingResponse;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

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
    public static final String TAG = PlaylistPresenter.class.getSimpleName();

    private PlaylistContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private final OkHttpClient mClient;

    private WebSocket mWebSocket;

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
    }

    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    public void onOpen(WebSocket webSocket, Response response) {
        LogUtils.i(TAG, "onOpen");
    }

    /**
     * Invoked when a text (type {@code 0x1}) message has been received.
     */
    public void onMessage(WebSocket webSocket, String text) {
        LogUtils.i(TAG, "onMessage string : " + text);
    }

    /**
     * Invoked when a binary (type {@code 0x2}) message has been received.
     */
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        LogUtils.i(TAG, "onMessage bytestring : " + bytes.toString());
    }

    /**
     * Invoked when the peer has indicated that no more incoming messages will be transmitted.
     */
    public void onClosing(WebSocket webSocket, int code, String reason) {
        LogUtils.i(TAG, "onClosing code : " + code + ",reason :" + reason);
    }

    /**
     * Invoked when both peers have indicated that no more messages will be transmitted and the
     * connection has been successfully released. No further calls to this listener will be made.
     */
    public void onClosed(WebSocket webSocket, int code, String reason) {
        LogUtils.i(TAG, "onClosed code : " + code + ",reason :" + reason);
    }

    /**
     * Invoked when a web socket has been closed due to an error reading from or writing to the
     * network. Both outgoing and incoming messages may have been lost. No further calls to this
     * listener will be made.
     */
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
    public void loadNowPlaying(String storeId) {
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
    public void loadAudients() {
        Disposable disposable = mRepository.getAudientTests()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<TencentMusic>>() {
                    @Override
                    public void onNext(List<TencentMusic> audients) {
                        if (mView.isActive()) {
                            mView.showProgressBar(false);

                            mView.showAudients(audients);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadAudients error " + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void thumbUpSong(TencentMusic audient) {
        LogUtils.i(TAG, "thumbUp");
    }

    @Override
    public void sendCommand(String command) {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.action = command;
        commandRequest.store = "4ca2e1a2-d5cc-490a-8371-63d8010a3964";
        String message = new Gson().toJson(commandRequest);

        LogUtils.i(TAG, "senCommand : " + message);

        mWebSocket.send(message);
    }
}
