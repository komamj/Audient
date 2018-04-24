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
package com.xinshang.audient.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xinshang.audient.R;
import com.xinshang.audient.base.AudientAdapter;
import com.xinshang.audient.favorite.MyFavoritesActivity;
import com.xinshang.audient.hint.HintDialogFragment;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.payment.PaymentDialogFragment;
import com.xinshang.audient.widget.AudientItemDecoration;
import com.xinshang.common.base.BaseFragment;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import java.util.List;

import butterknife.BindView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class SearchFragment extends BaseFragment implements SearchContract.View, SearchActivity.OnSearchListener {
    private static final String TAG = SearchFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.group_loading)
    Group mLoadingMore;
    @BindView(R.id.tv_empty)
    TextView mEmptyView;

    private AudientAdapter mAdapter;

    private SearchContract.Presenter mPresenter;

    private boolean mIsLoadingMore;

    private String mKeyword;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        LogUtils.i(TAG, "onAttach");

        ((SearchActivity) mContext).setListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");
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

        setLoadingIndicator(false);
        setLoadingMoreIndicator(false);

        mAdapter = new AudientAdapter(mContext);
        mAdapter.setEventListener(new AudientAdapter.EventListener() {
            @Override
            public void onFavoriteMenuClick(Audient audient) {
                Intent intent = new Intent(mContext, MyFavoritesActivity.class);
                intent.putExtra(Constants.KEY_AUDIENT, audient);
                mContext.startActivity(intent);
            }

            @Override
            public void onPlaylistChanged(Audient audient) {
                if (mPresenter != null) {
                    mPresenter.demand(audient);
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtils.i(TAG, "onScrollStateChanged newState : " + newState);

                if (mIsLoadingMore) {
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LogUtils.i(TAG, "onActivityCreated");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
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

        if (forceShow) {
            mRecyclerView.setVisibility(View.GONE);

            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }


    @Override
    public void setLoadingIndicator(final boolean isActive) {
        LogUtils.i(TAG, "showProressBar forceShow :" + isActive);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(isActive);
            }
        });

        showEmpty(false);
    }

    @Override
    public void setLoadingMoreIndicator(final boolean isActive) {
        mIsLoadingMore = isActive;

        mLoadingMore.post(new Runnable() {
            @Override
            public void run() {
                if (isActive) {
                    mLoadingMore.setVisibility(View.VISIBLE);
                } else {
                    mLoadingMore.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void showNoMoreMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.no_more_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showPaymentDialog(Audient audient) {
        PaymentDialogFragment.show(getChildFragmentManager(), audient);
    }

    @Override
    public void showHintDialog(Audient audient) {
        HintDialogFragment.show(getChildFragmentManager(), audient);
    }

    @Override
    public void showAudients(List<Audient> audients) {
        LogUtils.i(TAG, "showFavoritesSong count:" + audients.size());

        mRecyclerView.setVisibility(View.VISIBLE);

        mAdapter.replace(audients);
    }

    @Override
    public void showNextPageSongs(List<Audient> songs) {
        mAdapter.appendData(songs);
    }

    @Override
    public void onSearch(String keyword) {
        mKeyword = keyword;

        if (mPresenter != null) {
            mPresenter.loadSongs(keyword);
        }
    }
}
