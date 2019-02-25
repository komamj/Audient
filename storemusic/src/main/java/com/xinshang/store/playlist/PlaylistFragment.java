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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.data.entities.StoreSong;
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
    private static final String SHUFFLE_MODE = "random";
    private static final String REPEAT_MODE = "sequence";

    private boolean isShuffleMode = false;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.iv_pause)
    ImageView mPause;
    @BindView(R.id.iv_stop)
    ImageView mStop;
    @BindView(R.id.iv_next)
    ImageView mNext;
    @BindView(R.id.iv_shuffle)
    ImageView mPlayMode;
    @BindView(R.id.progress_bar)
    CircularProgressBar progressBar;

    @Inject
    PlaylistPresenter mPresenter;

    private PlaylistAdapter mAdapter;

    private LocalBroadcastManager mLocalBroadcastManager;

    private LocalReceiver mLocalReceiver;

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
            setNextActive(false);
            mPresenter.next();
        }
    }

    @OnClick(R.id.iv_shuffle)
    void onShuffleClick() {
        if (mPresenter != null) {
            if (isShuffleMode) {
                mPresenter.shuffle(REPEAT_MODE);
                mPlayMode.setImageResource(R.drawable.ic_repeat);
                isShuffleMode = false;
            } else {
                mPresenter.shuffle(SHUFFLE_MODE);
                mPlayMode.setImageResource(R.drawable.ic_shuffle);
                isShuffleMode = true;
            }
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

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);

        mLocalReceiver = new LocalReceiver();

        setStopActive(false);
        setNextActive(false);
        setPauseActive(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.unSubscribe();
                    mPresenter.subscribe();
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);

        setLoadingIndicator(true);

        mAdapter = new PlaylistAdapter(mContext);
        mAdapter.setEventListener(new PlaylistAdapter.EventListener() {
            @Override
            public void onFavoriteMenuClick(Song audient) {
                Intent intent = new Intent(mContext, MyFavoritesActivity.class);
                intent.putExtra(Constants.KEY_AUDIENT, audient);
                mContext.startActivity(intent);
            }

            @Override
            public void onDeleteMenuClick(StoreSong storeSong) {
                if (mPresenter != null) {
                    if (TextUtils.isEmpty(storeSong.demandId)) {
                        mPresenter.deleteStoreSong(storeSong, null);
                    } else {
                        ReasonDialogFragment.showReasonDialog(getChildFragmentManager(), storeSong.id);
                    }
                }
            }

            @Override
            public void onItemClick(final StoreSong storeSong) {
                PlayItemDialogFragment.showDialog(getChildFragmentManager(),
                        new PlayItemDialogFragment.OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                if (mPresenter != null) {
                                    mPresenter.play(storeSong.id);
                                }
                            }
                        }, mContext.getString(R.string.play_specified_item));
            }
        });

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        LogUtils.i(TAG, "onResume");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_REBOOT);
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, intentFilter);

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == Constants.ACTION_REBOOT) {
                if (mPresenter != null) {
                    mPresenter.reboot();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        LogUtils.i(TAG, "onPause");

        mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setNextActive(boolean active) {
        mNext.setEnabled(active);
    }

    @Override
    public void setStopActive(boolean active) {
        mStop.setEnabled(active);
    }

    @Override
    public void setPauseActive(boolean active) {
        mPause.setEnabled(active);
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
    public void setLoadingIndicator(boolean isActive) {
        mSwipeRefreshLayout.setRefreshing(isActive);
    }

    @Override
    public void showNowPlaying(StoreSong storeSong) {
        if (storeSong == null) {
            return;
        }

        GlideApp.with(this)
                .asBitmap()
                .circleCrop()
                .load(storeSong)
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_album)
                .into(mAlbum);

        mName.setText(storeSong.mediaName);

        progressBar.setProgressMax(Integer.parseInt(storeSong.mediaInterval));

        LogUtils.d(TAG, "showNowPlaying :" + Integer.parseInt(storeSong.mediaInterval));
    }

    @Override
    public void showPlaylist(List<StoreSong> storePlaylists) {
        mAdapter.replace(storePlaylists);

        for (int i = 0; i < storePlaylists.size(); i++) {
            if (storePlaylists.get(i).isPlaying) {
                mRecyclerView.scrollToPosition(i);
                break;
            }
        }
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
    public void updateProgress(int progress) {
        LogUtils.d(TAG, "updateProgress :" + progress);
        progressBar.setProgress(progress);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_playlist;
    }

    @Override
    public void setPresenter(PlaylistContract.Presenter presenter) {

    }
}
