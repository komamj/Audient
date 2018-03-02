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
package com.xinshang.store.audition;

import android.media.MediaPlayer;
import android.text.TextUtils;

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.File;
import com.xinshang.store.data.entities.FileResult;
import com.xinshang.store.data.entities.SongDetailResult;
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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class AuditionPresenter implements AuditionContract.Presenter,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener {
    private static final String TAG = AuditionPresenter.class.getSimpleName();

    private final AuditionContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private MediaPlayer mMediaPlayer;

    @Inject
    public AuditionPresenter(AuditionContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();
    }

    @Inject
    void setUpListener() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        LogUtils.i(TAG, "subscribe");

        loadAudient(mView.getAudientId());

        loadFile(mView.getAudientId());
    }

    private void initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
        }
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        release();

        mDisposables.clear();
    }

    @Override
    public void loadAudient(String id) {
        Disposable disposable = mRepository.getSongDetailResult(id)
                .map(new Function<SongDetailResult, TencentMusic>() {
                    @Override
                    public TencentMusic apply(SongDetailResult songDetailResult) throws Exception {
                        return songDetailResult.audient;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<TencentMusic>() {
                    @Override
                    public void onNext(TencentMusic audient) {
                        mView.showAudient(audient);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadNowPlaying onError:" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void loadMedia(String url) {
        initializeMediaPlayer();

        try {
            mMediaPlayer.setDataSource(url);
        } catch (Exception e) {
            LogUtils.e(TAG, "loadMedia error :" + e.toString());
        }

        try {
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            LogUtils.e(TAG, "loadMedia prepare error :" + e.toString());
        }
    }

    @Override
    public void loadFile(String id) {
        Disposable disposable = mRepository.getFileResult(id)
                .map(new Function<FileResult, List<File>>() {
                    @Override
                    public List<File> apply(FileResult fileResult) throws Exception {
                        return fileResult.files;
                    }
                })
                .flatMap(new Function<List<File>, Publisher<File>>() {
                    @Override
                    public Publisher<File> apply(List<File> files) throws Exception {
                        for (File file : files) {
                            if (!TextUtils.isEmpty(file.url)) {
                                return Flowable.just(file);
                            }
                        }
                        return Flowable.fromIterable(files);
                    }
                })
                .map(new Function<File, String>() {
                    @Override
                    public String apply(File file) throws Exception {
                        return file.url;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<String>() {
                    @Override
                    public void onNext(String url) {
                        LogUtils.i(TAG, "url :" + url);

                        loadMedia(url);
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
    public void pause() {
        if (mMediaPlayer == null) {
            return;
        }

        mMediaPlayer.pause();

        if (mView.isActive()) {
            mView.updateControllView(Constants.PAUSED);
        }
    }

    @Override
    public void play() {
        if (mMediaPlayer == null) {
            return;
        }

        mMediaPlayer.start();

        if (mView.isActive()) {
            mView.updateControllView(Constants.PLAYING);
        }

        refreshProgress();
    }

    @Override
    public void stop() {
        if (mMediaPlayer == null) {
            return;
        }

        mMediaPlayer.stop();

        if (mView.isActive()) {
            mView.updateControllView(Constants.PAUSED);
        }

        refreshProgress();
    }

    @Override
    public void doPauseOrPlay() {
        if (isCompleted()) {
            replay();
        } else if (isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    private void refreshProgress() {
        Disposable disposable = Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        if (isPlaying()) {
                            int currentPosition = getCurrentPosition();

                            if (currentPosition >= mView.getLimitedTime()) {
                                pause();

                                if (mView.isActive()) {
                                    mView.updateControllView(Constants.COMPLETED);
                                }
                            }

                            if (mView.isActive()) {
                                mView.showProgress(currentPosition);
                            }
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

    private boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }

        return false;
    }

    private boolean isCompleted() {
        if (getCurrentPosition() >= mView.getLimitedTime()) {
            return true;
        }

        return false;
    }

    @Override
    public void seekTo(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    private int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }

        return 0;
    }

    @Override
    public void replay() {
        seekTo(0);

        play();
    }

    private void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int position) {
        LogUtils.i(TAG, "onBufferingUpdate position :" + position);

        if (mView.isActive()) {
            mView.showSecondaryProgress(position);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        /*int duration = mMediaPlayer.getDuration();

        if (mView.isActive()) {
            mView.setMaxProgress(duration);
        }*/

        play();
    }
}
