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
package com.xinshang.audient.nowplaying;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xinshang.audient.R;
import com.xinshang.audient.comment.CommentActivity;
import com.xinshang.audient.helper.GlideApp;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.Lyric;
import com.xinshang.common.base.BaseFragment;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class NowPlayingFragment extends BaseFragment implements NowPlayingContract.View {
    private static final String TAG = NowPlayingFragment.class.getSimpleName();

    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.progress_bar)
    ProgressBar mProgress;
    @BindView(R.id.tv_current)
    TextView mCurrent;
    @BindView(R.id.tv_duration)
    TextView mDuration;
    @BindView(R.id.tv_name)
    TextView mMusicName;
    @BindView(R.id.tv_singer_name)
    TextView mSingerName;
    @BindView(R.id.fab_thumb_up)
    FloatingActionButton mFabThumbUp;

    private boolean mIsVoted;

    private String mStoreId;

    @OnClick(R.id.fab_next)
    void skipNext() {
        if (mPresenter != null) {
            mPresenter.playNext();
        }
    }

    @OnClick(R.id.fab_thumb_up)
    void thumbUp() {
        if (mPresenter != null) {
            if (mIsVoted) {
                mPresenter.cancelThumbUpSong(mStoreId, mAudient);
            } else {
                mPresenter.thumbUpSong(mAudient, mStoreId);
            }
        }

        showVoteInfo(!mIsVoted);
    }

    @OnClick(R.id.iv_favorite)
    void processFavorite() {

    }

    @OnClick(R.id.iv_comment)
    void processComment() {
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra(Constants.KEY_AUDIENT, mAudient);
        startActivity(intent);
    }

    private Audient mAudient;

    private NowPlayingContract.Presenter mPresenter;

    public NowPlayingFragment() {
    }

    public static NowPlayingFragment newInstance() {
        NowPlayingFragment fragment = new NowPlayingFragment();
        return fragment;
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
    }

    @Override
    public void onResume() {
        super.onResume();

        LogUtils.i(TAG, "onResume");

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        LogUtils.i(TAG, "onPause");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_playing;
    }

    @Override
    public void setPresenter(NowPlayingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showVoteInfo(boolean isVoted) {
        mIsVoted = isVoted;
        if (isVoted) {
            mFabThumbUp.setImageResource(R.drawable.ic_thumb_up_black);
        } else {
            mFabThumbUp.setImageResource(R.drawable.ic_thumb_up_white);
        }
    }

    @Override
    public void showEmpty(boolean forceShow) {

    }

    @Override
    public void showProgressBar(boolean forceShow) {

    }

    @Override
    public void showNowPlaying(Audient audient) {
        mAudient = audient;

        GlideApp.with(this)
                .asDrawable()
                .thumbnail(0.1f)
                .placeholder(new ColorDrawable(Color.GRAY))
                .load(audient)
                .into(mAlbum);

        mDuration.setText(DateUtils.formatElapsedTime(audient.duration));
        mMusicName.setText(audient.mediaName);
        mSingerName.setText(audient.artistName);
    }

    @Override
    public void showLyric(Lyric lyric) {
       // LogUtils.i(TAG, "showLyric :" + lyric.lyric);
    }
}
