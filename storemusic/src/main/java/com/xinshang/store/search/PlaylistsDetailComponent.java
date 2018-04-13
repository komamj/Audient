package com.xinshang.store.search;

import com.xinshang.store.data.AudientRepositoryComponent;
import com.xinshang.store.utils.ActivityScoped;

import dagger.Component;

/**
 * Created by koma on 4/13/18.
 */

@ActivityScoped
@Component(dependencies = AudientRepositoryComponent.class,
        modules = PlaylistsDetailPresenterModule.class)
public interface PlaylistsDetailComponent {
    void inject(PlaylistsDetailActivity activity);
}
