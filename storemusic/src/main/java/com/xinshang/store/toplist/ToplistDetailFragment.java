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
package com.xinshang.store.toplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xinshang.store.R;
import com.xinshang.store.base.AudientAdapter;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.favorite.MyFavoritesActivity;
import com.xinshang.store.playlist.ConfirmDialog;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;
import com.xinshang.store.widget.AudientItemDecoration;

import java.util.List;

import butterknife.BindView;

public class ToplistDetailFragment extends BaseFragment implements ToplistDetailContract.View {
    private static final String TAG = ToplistDetailFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.loading_layout)
    View mLoading;

    private int mTopId;

    private boolean mIsLoading = false;

    private String mShowTime;

    private AudientAdapter mAdapter;

    private ToplistDetailContract.Presenter mPresenter;

    public ToplistDetailFragment() {
    }

    public static ToplistDetailFragment newInstance(int topId, String showTime) {
        ToplistDetailFragment fragment = new ToplistDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_TOP_ID, topId);
        bundle.putString(Constants.KEY_UPDATE, showTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mTopId = getArguments().getInt(Constants.KEY_TOP_ID);

            mShowTime = getArguments().getString(Constants.KEY_UPDATE);
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
                    mPresenter.loadToplistSongs(mTopId, mShowTime);
                }
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
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

        /*mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LogUtils.i(TAG, "onScrollStateChanged newState : " + newState);

                if (mIsLoading) {
                    return;
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = layoutManager
                        .findLastVisibleItemPosition();
                if (lastPosition == mAdapter.getItemCount() - 1) {
                    // load next page
                    if (mPresenter != null) {
                        mIsLoading = true;

                        mPresenter.loadNextPage(mTopId, mShowTime);
                    }
                }
            }
        });*/
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.loadToplistSongs(mTopId, mShowTime);
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
        return R.layout.fragment_loading;
    }

    @Override
    public void setPresenter(ToplistDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void showToplistSongs(List<Song> audients) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter.update(audients);
    }

    @Override
    public void showNextPageSongs(List<Song> songs) {
        mIsLoading = false;

        mAdapter.appendData(songs);
    }

    @Override
    public void setLoadingIndicator(final boolean isActive) {
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
    public void showPlaySuccessfulMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.added_successful_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showPlayFailedMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.added_failed_message, Snackbar.LENGTH_SHORT)
                .show();
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
