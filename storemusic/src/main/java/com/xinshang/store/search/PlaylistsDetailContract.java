package com.xinshang.store.search;

import com.xinshang.store.base.BasePresenter;
import com.xinshang.store.base.BaseView;
import com.xinshang.store.data.entities.Playlist;
import com.xinshang.store.data.entities.Song;

import java.util.List;

public interface PlaylistsDetailContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void setLoadingIndicator(boolean isActive);

        void showSongs(List<Song> songs);
    }

    interface Presenter extends BasePresenter {
        void loadPlaylistSongs(Playlist playlist);

        void addToPlaylist(Song tencentMusic);

        void playAll();
    }
}
