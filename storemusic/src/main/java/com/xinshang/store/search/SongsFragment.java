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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.AudientAdapter;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.favorite.MyFavoritesActivity;
import com.xinshang.store.playlist.ConfirmDialog;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;
import com.xinshang.store.widget.AudientItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by koma on 4/11/18.
 */

public class SongsFragment extends BaseFragment implements SongsContract.View,
        SearchActivity.OnSearchListener {
    private static final String TAG = SongsFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.loading_layout)
    View mLoading;

    private TextView mEmpty;

    private boolean mIsPrepared;
    private boolean mIsLoaded;
    private boolean mIsLoading = false;

    private String mKeyword;

    private AudientAdapter mAdapter;

    @Inject
    SongsPresenter mPresenter;

    public static SongsFragment newInstance() {
        return new SongsFragment();
    }

    public SongsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        LogUtils.i(TAG, "onAttach");

        ((SearchActivity) mContext).addListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        DaggerSongsComponent.builder()
                .audientRepositoryComponent(
                        ((StoreMusicApplication) ((SearchActivity) mContext).getApplication()).getRepositoryComponent())
                .songsPresenterModule(new SongsPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        LogUtils.i(TAG, "setUserVisibleHint isVisibleToUser :" + isVisibleToUser);

        if (isVisibleToUser && mIsPrepared && !mIsLoaded) {
            if (mPresenter != null) {
                mPresenter.loadSongs(mKeyword);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadSongs(mKeyword);
                }
            }
        });

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

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LogUtils.i(TAG, "onScrollStateChanged newState : " + newState);

                if (mIsLoading) {
                    return;
                }

                if (newState == SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager)
                            recyclerView.getLayoutManager();
                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == mAdapter.getItemCount() - 1) {
                        // load next page
                        if (mPresenter != null) {
                            mPresenter.loadNextPageSongs(mKeyword);
                        }
                    }
                }
            }
        });
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        mIsPrepared = true;
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
    public void onDetach() {
        super.onDetach();

        LogUtils.i(TAG, "onDetach");

        ((SearchActivity) mContext).removeListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_loading;
    }

    @Override
    public void setPresenter(SongsContract.Presenter presenter) {
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showEmpty(boolean forceShow) {
        if (forceShow) {
            mRecyclerView.setVisibility(View.GONE);

            mEmpty.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void setLoadingIndictor(final boolean isActive) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void setLoadingMoreIndicator(final boolean isActive) {
        mIsLoading = isActive;

        mLoading.post(new Runnable() {
            @Override
            public void run() {
                if (isActive) {
                    mLoading.setVisibility(View.VISIBLE);
                } else {
                    mLoading.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void showSongs(List<Song> audients) {
        mIsLoaded = true;

        mRecyclerView.setVisibility(View.VISIBLE);

        mAdapter.replace(audients);
    }

    @Override
    public void showNextPageSongs(List<Song> songs) {
        mRecyclerView.setVisibility(View.VISIBLE);

        mAdapter.appendData(songs);
    }

    @Override
    public void onSearch(String keyword) {
        mKeyword = keyword;

        mIsLoaded = false;

        if (getUserVisibleHint() && mIsPrepared && !mIsLoaded) {
            if (mPresenter != null) {
                mPresenter.loadSongs(mKeyword);
            }
        }
    }

    @Override
    public void showNoMoreMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.no_more_message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
