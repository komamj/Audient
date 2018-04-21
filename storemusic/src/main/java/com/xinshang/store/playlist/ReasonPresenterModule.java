package com.xinshang.store.playlist;

import dagger.Module;
import dagger.Provides;

@Module
public class ReasonPresenterModule {
    private final ReasonContract.View mView;

    public ReasonPresenterModule(ReasonContract.View view) {
        mView = view;
    }

    @Provides
    ReasonContract.View provideReasonContractView() {
        return this.mView;
    }
}
