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
package com.xinshang.store.mine;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xinshang.store.R;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.Favorite;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.playlist.ConfirmDialog;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;
import com.xinshang.store.widget.AudientItemDecoration;

import java.util.List;

import butterknife.BindView;

public class FavoriteDetailFragment extends BaseFragment implements FavoriteDetailContract.View {
    private static final String TAG = FavoriteDetailFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private FavoritesSongAdapter mAdapter;

    private Favorite mFavorite;

    private FavoriteDetailContract.Presenter mPresenter;

    public FavoriteDetailFragment() {
    }

    public static FavoriteDetailFragment newInstance(Favorite favorite) {
        FavoriteDetailFragment fragment = new FavoriteDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_FAVORITE, favorite);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mFavorite = getArguments().getParcelable(Constants.KEY_FAVORITE);
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
                    mPresenter.loadData(mFavorite.favoritesId);
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary,
                R.color.colorAccent);

        setLoadingIndicator(true);

        mAdapter = new FavoritesSongAdapter(mContext);
        mAdapter.setEventListener(new FavoritesSongAdapter.EventListener() {
            @Override
            public void onDeleteEventChanged(Favorite.FavoritesSong favoritesSong) {
                if (mPresenter != null) {
                    mPresenter.deleteFavoriteSong(favoritesSong);
                }
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
            mPresenter.loadData(mFavorite.favoritesId);
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
    public void setPresenter(FavoriteDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public String getFavoritesId() {
        return this.mFavorite.favoritesId;
    }

    @Override
    public void showFavoritesSong(List<Favorite.FavoritesSong> favoritesSongs) {
        mAdapter.replace(favoritesSongs);
    }

    @Override
    public void showLoadingError() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.loading_error_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showAddSuccessfulMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.added_successful_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showAddFailedMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.added_failed_message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
