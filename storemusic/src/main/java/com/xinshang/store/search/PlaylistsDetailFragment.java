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
package com.xinshang.store.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xinshang.store.R;
import com.xinshang.store.base.AudientAdapter;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.Playlist;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.favorite.MyFavoritesActivity;
import com.xinshang.store.playlist.ConfirmDialog;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;
import com.xinshang.store.widget.AudientItemDecoration;

import java.util.List;

import butterknife.BindView;

public class PlaylistsDetailFragment extends BaseFragment implements PlaylistsDetailContract.View {
    private static final String TAG = PlaylistsDetailFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private AudientAdapter mAdapter;

    private PlaylistsDetailContract.Presenter mPresenter;

    private Playlist mPlaylist;

    public static PlaylistsDetailFragment newInstance(Playlist playlist) {
        PlaylistsDetailFragment fragment = new PlaylistsDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_PLAYLISTS, playlist);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPlaylist = getArguments().getParcelable(Constants.KEY_PLAYLISTS);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadPlaylistSongs(mPlaylist);
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);

        setLoadingIndicator(true);

        mAdapter = new AudientAdapter(mContext);
        mAdapter.setEventListener(new AudientAdapter.EventListener() {
            @Override
            public void onFavoriteMenuClick(Song audient) {
                Intent intent = new Intent(mContext, MyFavoritesActivity.class);
                intent.putExtra(Constants.KEY_AUDIENT, audient);
                mContext.startActivity(intent);
            }

            @Override
            public void onPlaylistChanged(final Song audient) {
                ConfirmDialog.showConfirmDialog(getChildFragmentManager(),
                        new ConfirmDialog.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                if (mPresenter != null) {
                                    mPresenter.addToPlaylist(audient);
                                }
                            }
                        }, mContext.getString(R.string.action_playlist));
            }
        });

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.loadPlaylistSongs(mPlaylist);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LogUtils.i(TAG, "onDestroyView");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadingIndicator(final boolean isActive) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void showSongs(List<Song> songs) {
        mAdapter.replace(songs);
    }

    @Override
    public void setPresenter(PlaylistsDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
