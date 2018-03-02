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
package com.xinshang.store.nowplaying;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xinshang.store.R;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.comment.CommentActivity;
import com.xinshang.store.data.entities.Lyric;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

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
    private TencentMusic mAudient;
    private NowPlayingContract.Presenter mPresenter;

    public NowPlayingFragment() {
    }

    public static NowPlayingFragment newInstance() {
        NowPlayingFragment fragment = new NowPlayingFragment();
        return fragment;
    }

    @OnClick(R.id.fab_next)
    void skipNext() {

    }

    @OnClick(R.id.fab_thumb_up)
    void thumbUp() {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");
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
    public void showEmpty(boolean forceShow) {

    }

    @Override
    public void showProgressBar(boolean forceShow) {

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
    public void showNowPlaying(TencentMusic audient) {
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
        LogUtils.i(TAG, "showLyric :" + lyric.lyric);
    }
}
