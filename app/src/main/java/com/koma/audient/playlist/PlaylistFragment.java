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
package com.koma.audient.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.koma.audient.AudientApplication;
import com.koma.audient.R;
import com.koma.audient.model.entities.AudientTest;
import com.koma.audient.nowplaying.NowPlayingActivity;
import com.koma.audient.widget.AudientItemDecoration;
import com.koma.common.base.BaseFragment;
import com.koma.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class PlaylistFragment extends BaseFragment implements PlaylistContract.View {
    private static final String TAG = PlaylistFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsPrepared;

    @OnClick(R.id.fab)
    void launchNowPlayingUI() {
        Intent intent = new Intent(mContext, NowPlayingActivity.class);
        startActivity(intent);
    }

    private PlaylistAdapter mAdapter;

    @Inject
    PlaylistPresenter mPresenter;

    public PlaylistFragment() {
    }

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        DaggerPlatlistComponent.builder()
                .audientRepositoryComponent(
                        (((AudientApplication) getActivity().getApplication()).getRepositoryComponent()))
                .playlistPresenterModule(new PlaylistPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        LogUtils.i(TAG, "setUserVisibleHint isVisibleToUser :" + isVisibleToUser);

        if (isVisibleToUser && mIsPrepared) {
            if (mPresenter != null) {
                mPresenter.subscribe();
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadAudients();
                }
            }
        });

        mAdapter = new PlaylistAdapter(mContext);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        mIsPrepared = true;

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
    public void showAudients(List<AudientTest> audientTests) {
        mSwipeRefreshLayout.setRefreshing(false);

        mAdapter.replace(audientTests);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_playlist;
    }

    @Override
    public void setPresenter(PlaylistContract.Presenter presenter) {

    }
}
