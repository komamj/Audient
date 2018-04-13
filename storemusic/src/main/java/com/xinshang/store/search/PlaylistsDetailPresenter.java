package com.xinshang.store.search;

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.PlayAllRequest;
import com.xinshang.store.data.entities.Playlist;
import com.xinshang.store.data.entities.PlaylistSongResponse;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class PlaylistsDetailPresenter implements PlaylistsDetailContract.Presenter {
    private static final String TAG = PlaylistsDetailPresenter.class.getSimpleName();

    private final PlaylistsDetailContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    private List<Song> mSongs;

    @Inject
    public PlaylistsDetailPresenter(PlaylistsDetailContract.View view,
                                    AudientRepository repository) {
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
    public void loadPlaylistSongs(final Playlist playlist) {
        if (mView.isActive()) {
            mView.setLoadingIndicator(true);
        }

        Disposable disposable = mRepository.getPlaylistDetails(playlist.id)
                .map(new Function<ApiResponse<PlaylistSongResponse>, List<Song>>() {
                    @Override
                    public List<Song> apply(ApiResponse<PlaylistSongResponse> playlistSongResponseApiResponse) throws Exception {
                        return playlistSongResponseApiResponse.data.tencentMusics;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Song>>() {
                    @Override
                    public void onNext(List<Song> songs) {
                        mSongs = songs;

                        if (mView.isActive()) {
                            mView.showSongs(songs);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadPlaylistSongs error : " + t.getMessage());

                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void addToPlaylist(Song tencentMusic) {
        Music music = new Music();
        music.storeId = mRepository.getStoreId();
        music.albumId = tencentMusic.albumId;
        music.albumName = tencentMusic.albumName;
        music.artistId = tencentMusic.artistId;
        music.artistName = tencentMusic.artistName;
        music.mediaInterval = String.valueOf(tencentMusic.duration);
        music.mediaId = tencentMusic.mediaId;
        music.mediaName = tencentMusic.mediaName;

        mRepository.addToPlaylist(music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "addToPlaylist completed");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "addToPlaylist completed");
                    }
                });
    }

    @Override
    public void playAll() {
        if (mSongs == null || mSongs.isEmpty()) {
            return;
        }

        PlayAllRequest playAllRequest = new PlayAllRequest();
        playAllRequest.songs = mSongs;

        String storeId = mRepository.getStoreId();

        mRepository.playAllSongs(storeId, playAllRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
