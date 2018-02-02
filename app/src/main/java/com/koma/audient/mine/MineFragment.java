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
package com.koma.audient.mine;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.koma.audient.AudientApplication;
import com.koma.audient.R;
import com.koma.audient.base.AudientAdapter;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.widget.AudientItemDecoration;
import com.koma.common.base.BaseFragment;
import com.koma.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MineFragment extends BaseFragment implements MineContract.View {
    private static final String TAG = MineFragment.class.getSimpleName();

    @BindView(R.id.recycler_view_favorite)
    RecyclerView mRecyclerViewFavorite;
    @BindView(R.id.recycler_view_dynamic)
    RecyclerView mRecyclerViewUser;
    @BindView(R.id.progress_bar_favorite)
    ContentLoadingProgressBar mFavoriteProgressBar;
    @BindView(R.id.progress_bar_user)
    ContentLoadingProgressBar mUserProgressBar;

    private boolean mIsPrepared;

    private AudientAdapter mAdapter;

    private FavoriteAdapter mFavoriteAdapter;

    @Inject
    MinePresenter mPresenter;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mIsPrepared) {
            if (mPresenter != null) {
                mPresenter.subscribe();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        DaggerMineComponent.builder()
                .audientRepositoryComponent(
                        (((AudientApplication) getActivity().getApplication()).getRepositoryComponent()))
                .minePresenterModule(new MinePresenterModule(this))
                .build()
                .inject(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mAdapter = new AudientAdapter(mContext);

        mFavoriteAdapter = new FavoriteAdapter(mContext);

        LinearLayoutManager layoutManagerFavorite = new LinearLayoutManager(mContext);
        layoutManagerFavorite.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewFavorite.setLayoutManager(layoutManagerFavorite);
        mRecyclerViewFavorite.setAdapter(mFavoriteAdapter);

        LinearLayoutManager layoutManagerUser = new LinearLayoutManager(mContext);
        layoutManagerUser.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewUser.setLayoutManager(layoutManagerUser);
        mRecyclerViewUser.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerViewUser.setAdapter(mAdapter);

        mIsPrepared = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtils.i(TAG, "onDestroy");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {

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

    }

    @Override
    public void showFavoriteProgressBar(boolean forceShow) {
        if (forceShow) {
            mFavoriteProgressBar.show();
        } else {
            mFavoriteProgressBar.hide();
        }
    }

    @Override
    public void showUserProgressBar(boolean forceShow) {
        if (forceShow) {
            mUserProgressBar.show();
        } else {
            mUserProgressBar.hide();
        }
    }


    @Override
    public void showFavorite(List<Audient> audients) {

    }

    @Override
    public void showUser(List<Audient> audients) {
        mAdapter.replace(audients);
    }
}
