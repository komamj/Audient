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
package com.xinshang.audient.toplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

public class ToplistDetailFragment extends BaseFragment implements ToplistDetailContract.View {
    private static final String TAG = ToplistDetailFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int mTopId;

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
                    mPresenter.subscribe();
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);

        mSwipeRefreshLayout.setRefreshing(true);

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

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base;
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
    public int getTopId() {
        return this.mTopId;
    }

    @Override
    public String getShowTime() {
        return this.mShowTime;
    }

    @Override
    public void showToplistDetail(List<Audient> audients) {
        mSwipeRefreshLayout.setRefreshing(false);

        mAdapter.update(audients);
    }

    @Override
    public void showPaymentDialog(Audient audient) {
        PaymentDialogFragment.show(getChildFragmentManager(), audient);
    }

    @Override
    public void showHintDialog(Audient audient) {
        HintDialogFragment.show(getChildFragmentManager(), audient);
    }
}
