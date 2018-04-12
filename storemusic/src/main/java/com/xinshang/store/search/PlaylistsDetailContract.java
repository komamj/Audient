package com.xinshang.store.search;

import com.xinshang.store.base.BasePresenter;
import com.xinshang.store.base.BaseView;
import com.xinshang.store.data.entities.TencentMusic;

public interface PlaylistsDetailContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadPlaylistSongs();

        void addToPlaylist(TencentMusic tencentMusic);
    }
}
