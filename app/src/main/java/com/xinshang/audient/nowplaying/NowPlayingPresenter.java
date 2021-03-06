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
package com.xinshang.audient.nowplaying;

import com.google.gson.Gson;
import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.CommandRequest;
import com.xinshang.audient.model.entities.Lyric;
import com.xinshang.audient.model.entities.LyricResult;
import com.xinshang.audient.model.entities.NowPlayingResponse;
import com.xinshang.audient.model.entities.StoreVoteResponse;
import com.xinshang.audient.model.entities.ThumbUpSongRequest;
import com.xinshang.audient.model.entities.VoteInfo;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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

public class NowPlayingPresenter extends WebSocketListener implements NowPlayingContract.Presenter {
    public static final String TAG = NowPlayingPresenter.class.getSimpleName();

    private NowPlayingContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private final OkHttpClient mClient;

    private WebSocket mWebSocket;

    @Inject
    public NowPlayingPresenter(NowPlayingContract.View view, AudientRepository repository) {
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

        loadNowPlaying();
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

        if (mWebSocket != null) {
            mWebSocket.cancel();
        }

        mDisposables.clear();
    }

    @Override
    public void loadNowPlaying() {
        mDisposables.clear();

        Disposable disposable = mRepository.getNowPlayingResult()
                .map(new Function<NowPlayingResponse, Audient>() {
                    @Override
                    public Audient apply(NowPlayingResponse nowPlayingResult) throws Exception {
                        return nowPlayingResult.audient;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Audient>() {
                    @Override
                    public void accept(Audient audient) throws Exception {
                        if (mView.isActive()) {
                            mView.showNowPlaying(audient);
                        }
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Function<Audient, Publisher<Lyric>>() {
                    @Override
                    public Publisher<Lyric> apply(Audient audient) throws Exception {
                        return mRepository.getLyricResult(audient.mediaId)
                                .map(new Function<LyricResult, Lyric>() {
                                    @Override
                                    public Lyric apply(LyricResult lyricResult) throws Exception {
                                        return lyricResult.lyric;
                                    }
                                });
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Lyric>() {
                    @Override
                    public void onNext(Lyric lyric) {
                        if (mView.isActive()) {
                            mView.showLyric(lyric);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadNowPlaying error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void loadVoteInfo(String mediaId, String storeId) {
        Disposable disposable = mRepository.getVoteInfo(mediaId, storeId)
                .map(new Function<StoreVoteResponse, VoteInfo>() {
                    @Override
                    public VoteInfo apply(StoreVoteResponse storeVoteResponse) throws Exception {
                        return storeVoteResponse.voteInfo;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<VoteInfo>() {
                    @Override
                    public void onNext(VoteInfo voteInfo) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadVoteInfo error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);

    }

    @Override
    public void thumbUpSong(Audient audient, String storeId) {
        ThumbUpSongRequest thumbUpSongRequest = new ThumbUpSongRequest();
        thumbUpSongRequest.mediaId = audient.mediaId;
        thumbUpSongRequest.albumId = audient.albumId;
        thumbUpSongRequest.albumName = audient.albumName;
        thumbUpSongRequest.artistName = audient.artistName;
        thumbUpSongRequest.mediaName = audient.mediaName;
        thumbUpSongRequest.storeId = storeId;

        mRepository.thumbUpSong(thumbUpSongRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "thumbUpSong error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void cancelThumbUpSong(String storeId, Audient audient) {
        mRepository.cancelThumbUpSong(storeId, audient.mediaId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "cancelThumbUpSong error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void playNext() {
        CommandRequest commandRequest = new CommandRequest();
        commandRequest.action = "next";
        commandRequest.store = "4ca2e1a2-d5cc-490a-8371-63d8010a3964";
        String command = new Gson().toJson(commandRequest);
        LogUtils.i(TAG, "playNext command : " + command);
        mWebSocket.send(command);
    }
}
