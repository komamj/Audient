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
package com.koma.audient.comment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.koma.audient.R;
import com.koma.audient.model.entities.Comment;
import com.koma.audient.widget.AudientItemDecoration;
import com.koma.common.base.BaseFragment;
import com.koma.common.util.Constants;
import com.koma.common.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentFragment extends BaseFragment implements CommentContract.View {
    private static final String TAG = CommentFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @OnClick(R.id.fab)
    void showEditView() {

    }

    private CommentAdapter mAdapter;

    private CommentContract.Presenter mPresenter;

    private long mId;

    public CommentFragment() {
    }

    public static CommentFragment newInstance(long id) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mId = getArguments().getLong(Constants.ID);
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
                    mPresenter.loadComments(mId);
                }
            }
        });

        mAdapter = new CommentAdapter(mContext);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);
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
    public void show(List<Comment> comments) {

    }
}
