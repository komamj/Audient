package com.xinshang.store.search;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by koma on 4/11/18.
 */

@Module
public class SongsPresenterModule {
    private final SongsContract.View mView;

    public SongsPresenterModule(@NonNull SongsContract.View view) {
        mView = view;
    }

    @Provides
    SongsContract.View provideSongsContractView() {
        return mView;
    }
}
