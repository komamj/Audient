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
package com.xinshang.store.comment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xinshang.store.R;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.Comment;
import com.xinshang.store.data.entities.CommentDataBean;
import com.xinshang.store.data.entities.MessageEvent;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;
import com.xinshang.store.widget.AudientItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class CommentFragment extends BaseFragment implements CommentContract.View {
    private static final String TAG = CommentFragment.class.getSimpleName();

    @BindView(R.id.tv_empty)
    TextView mEmptyView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CommentAdapter mAdapter;

    private CommentContract.Presenter mPresenter;

    private Song mAudient;

    public CommentFragment() {
    }

    public static CommentFragment newInstance(Song audient) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_AUDIENT, audient);
        fragment.setArguments(bundle);
        return fragment;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadComments(mAudient);
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);

        setLoadingIncator(true);

        mAdapter = new CommentAdapter(mContext);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        // load data
        if (mPresenter != null) {
            mPresenter.loadComments(mAudient);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.i(TAG, "onStart");
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        LogUtils.i(TAG, "onMessageEvent");

        if (TextUtils.equals(messageEvent.getMessage(), Constants.MESSAGE_COMMENT_CHANGED)) {
            if (mPresenter != null) {
                mPresenter.loadComments(mAudient);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
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
        return R.layout.fragment_comment;
    }

    @Override
    public void setPresenter(CommentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showComments(List<Comment> comments) {
        mAdapter.replace(comments);
    }

    @Override
    public void showCommentDataBean(CommentDataBean commentDataBean) {
        mAdapter.setCommentDataBean(commentDataBean);
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void showSuccessfulMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.loading_successful_message, Snackbar.LENGTH_SHORT)
                .show();
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
    public void showEmpty(boolean forceShow) {
        if (forceShow) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setLoadingIncator(boolean forceShow) {
        mSwipeRefreshLayout.setRefreshing(forceShow);
    }
}
