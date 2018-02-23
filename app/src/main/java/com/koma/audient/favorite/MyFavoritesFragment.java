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
package com.koma.audient.favorite;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.koma.audient.R;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.Favorite;
import com.koma.audient.widget.AudientItemDecoration;
import com.koma.common.base.BaseFragment;
import com.koma.common.util.Constants;
import com.koma.common.util.LogUtils;

import java.util.List;

import butterknife.BindView;

public class MyFavoritesFragment extends BaseFragment implements MyFavoritesContract.View {
    private static final String TAG = MyFavoritesFragment.class.getSimpleName();

    private static final String TAG_MY_FAVORITES = "my_favorites";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Context mContext;

    private Audient mAudient;

    private MyFavoritesAdapter mAdapter;

    private MyFavoritesContract.Presenter mPresenter;

    public MyFavoritesFragment() {
    }

    public static MyFavoritesFragment newInstance(Audient audient) {
        final MyFavoritesFragment dialogFragment = new MyFavoritesFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_AUDIENT, audient);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        LogUtils.i(TAG, "onAttach");

        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mAudient = getArguments().getParcelable(Constants.KEY_AUDIENT);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadMyFavorites();
                }
            }
        });

        setLoadingIndicator(true);

        mAdapter = new MyFavoritesAdapter(mContext);
        mAdapter.setListener(new MyFavoritesAdapter.EventListener() {
            @Override
            public void onItemClick(Favorite favorite) {
                if (mPresenter != null) {
                    mPresenter.addToFavorite(favorite.favoritesId, mAudient);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.loadMyFavorites();
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
    public void setPresenter(MyFavoritesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showFavorites(List<Favorite> favorites) {
        mAdapter.update(favorites);
    }
}
