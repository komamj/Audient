/*
 * Copyright 2017 Koma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinshang.store.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.StorePlaylist;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.favorite.MyFavoritesActivity;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;
import com.xinshang.store.widget.AudientItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaylistFragment extends BaseFragment implements PlaylistContract.View {
    private static final String TAG = PlaylistFragment.class.getSimpleName();

    private static final String COMMAND_NEXT = "next";
    private static final String COMMAND_STOP = "stop";
    private static final String COMMAND_PAUSE = "pause";
    private static final String COMMAND_PLAY = "play";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.iv_pause)
    ImageView mPause;

    @Inject
    PlaylistPresenter mPresenter;

    private PlaylistAdapter mAdapter;

    public PlaylistFragment() {
    }

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        return fragment;
    }

    @OnClick(R.id.iv_pause)
    void onPauseClick() {
        if (mPresenter != null) {
            mPresenter.doPauseOrPlay();
        }
    }

    @OnClick(R.id.iv_stop)
    void onStopClick() {
        if (mPresenter != null) {
            mPresenter.stop();
        }
    }

    @OnClick(R.id.iv_next)
    void onNextClick() {
        if (mPresenter != null) {
            mPresenter.next();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        DaggerPlatlistComponent.builder()
                .audientRepositoryComponent(
                        (((StoreMusicApplication) getActivity().getApplication()).getRepositoryComponent()))
                .playlistPresenterModule(new PlaylistPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProgressBar(true);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadStorePlaylist();
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);

        mAdapter = new PlaylistAdapter(mContext);
        mAdapter.setEventListener(new PlaylistAdapter.EventListener() {
            @Override
            public void onFavoriteMenuClick(TencentMusic audient) {
                Intent intent = new Intent(mContext, MyFavoritesActivity.class);
                intent.putExtra(Constants.KEY_AUDIENT, audient);
                mContext.startActivity(intent);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.loadStorePlaylist();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        LogUtils.i(TAG, "onResume");

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        LogUtils.i(TAG, "onPause");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void showLoadingError() {
        LogUtils.i(TAG, "showLoadingError");
    }

    @Override
    public void showEmpty(boolean forceShow) {
        LogUtils.i(TAG, "showEmpty forceShow :" + forceShow);
    }

    @Override
    public void showProgressBar(boolean forceShow) {
        LogUtils.i(TAG, "showProressBar forceShow :" + forceShow);
        if (forceShow) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }

    @Override
    public void showNowPlaying(StorePlaylist storePlaylist) {
        LogUtils.i(TAG,"showNowPlaying : "+ storePlaylist.mediaName);

        GlideApp.with(this)
                .asBitmap()
                .circleCrop()
                .load(storePlaylist)
                .thumbnail(0.1f)
                .transition(new BitmapTransitionOptions().crossFade())
                .placeholder(R.drawable.ic_album)
                .into(mAlbum);

        mName.setText(storePlaylist.mediaName);
    }

    @Override
    public void showPlaylist(List<StorePlaylist> storePlaylists) {
        mSwipeRefreshLayout.setRefreshing(false);

        mAdapter.replace(storePlaylists);
    }

    @Override
    public void updatePlayIcon(boolean isPlaying) {
        if (isPlaying) {
            mPause.setImageResource(R.drawable.ic_pause);
        } else {
            mPause.setImageResource(R.drawable.ic_play);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_playlist;
    }

    @Override
    public void setPresenter(PlaylistContract.Presenter presenter) {

    }
}
