package com.xinshang.audient.comment;

import com.xinshang.audient.model.AudientRepositoryComponent;
import com.xinshang.common.util.FragmentScoped;

import dagger.Component;

/**
 * Created by koma on 3/1/18.
 */
@FragmentScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = EditCommentPresenterModule.class)
public interface EditCommentComponent {
    void inject(EditCommentDialogFragment fragment);
}
