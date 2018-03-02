package com.xinshang.store.comment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by koma on 3/1/18.
 */

@Module
public class EditCommentPresenterModule {
    private final EditCommentContract.View mView;

    public EditCommentPresenterModule(EditCommentContract.View view) {
        this.mView = view;
    }

    @Provides
    EditCommentContract.View provideEditCommentContractView() {
        return this.mView;
    }
}
