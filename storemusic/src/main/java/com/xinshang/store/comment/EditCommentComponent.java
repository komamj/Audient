package com.xinshang.store.comment;

import com.xinshang.store.data.AudientRepositoryComponent;
import com.xinshang.store.utils.FragmentScoped;

import dagger.Component;

/**
 * Created by koma on 3/1/18.
 */
@FragmentScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = EditCommentPresenterModule.class)
public interface EditCommentComponent {
    void inject(EditCommentDialogFragment fragment);
}
