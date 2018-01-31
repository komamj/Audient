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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koma.audient.R;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.Comment;
import com.koma.audient.util.Utils;
import com.koma.audient.widget.AudientItemDecoration;
import com.koma.common.base.BaseFragment;
import com.koma.common.util.Constants;
import com.koma.common.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentFragment extends BaseFragment implements CommentContract.View {
    private static final String TAG = CommentFragment.class.getSimpleName();

    @BindView(R.id.tv_empty)
    TextView mEmptyView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.iv_favorite)
    ImageView mFavorite;
    @BindView(R.id.tv_name)
    TextView mMusicName;
    @BindView(R.id.tv_artist_name)
    TextView mArtistName;
    @BindView(R.id.edit_text)
    AppCompatEditText mEditText;

    @OnClick(R.id.iv_send)
    void onSendClick() {
        Comment comment = new Comment();
        comment.message = mEditText.getText().toString();
        comment.time = Utils.getTimeStamp();
        comment.userName = "Koma";

        mEditText.clearFocus();

        mAdapter.addComment(comment);
    }

    private CommentAdapter mAdapter;

    private CommentContract.Presenter mPresenter;

    private String mId;

    public CommentFragment() {
    }

    public static CommentFragment newInstance(String id) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_AUDIENT_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mId = getArguments().getString(Constants.KEY_AUDIENT_ID);
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

        // load data
        if (mPresenter != null) {
            mPresenter.loadAudient(mId);

            mPresenter.loadComments(mId);
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
        return R.layout.fragment_comment;
    }

    @Override
    public void setPresenter(CommentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getAudientId() {
        return this.mId;
    }

    @Override
    public void showAudient(Audient audient) {
        GlideApp.with(this)
                .load(audient)
                .placeholder(new ColorDrawable(Color.GRAY))
                .into(mAlbum);

        mMusicName.setText(audient.name);
        mArtistName.setText(audient.artist.name);
    }

    @Override
    public void showComments(List<Comment> comments) {
        mAdapter.replace(comments);
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
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgressBar(boolean forceShow) {
        mSwipeRefreshLayout.setRefreshing(forceShow);
    }
}
