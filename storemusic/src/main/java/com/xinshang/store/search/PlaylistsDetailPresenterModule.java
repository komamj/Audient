package com.xinshang.store.search;

import dagger.Module;
import dagger.Provides;

/**
 * Created by koma on 4/13/18.
 */

@Module
public class PlaylistsDetailPresenterModule {
    private final PlaylistsDetailContract.View mView;

    public PlaylistsDetailPresenterModule(PlaylistsDetailContract.View view) {
        mView = view;
    }

    @Provides
    PlaylistsDetailContract.View providePlaylistsDetailContractView() {
        return this.mView;
    }
}
