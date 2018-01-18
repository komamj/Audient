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
package com.koma.audient.nowplaying;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koma.audient.R;
import com.koma.audient.comment.CommentActivity;
import com.koma.audient.model.entities.MusicFileItem;
import com.koma.common.base.BaseFragment;
import com.koma.common.util.Constants;

import java.util.List;

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
    @BindView(R.id.tv_song)
    TextView mSong;
    @BindView(R.id.tv_singer)
    TextView mSinger;

    @OnClick(R.id.fab_next)
    void skipNext() {

    }

    @OnClick(R.id.iv_favorite)
    void processFavorite() {

    }

    @OnClick(R.id.iv_comment)
    void processComment() {
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra(Constants.ID, -1);
        startActivity(intent);
    }

    private NowPlayingContract.Presenter mPresenter;

    public NowPlayingFragment() {
    }

    public static NowPlayingFragment newInstance() {
        NowPlayingFragment fragment = new NowPlayingFragment();
        return fragment;
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
    public void showLoadingError() {

    }

    @Override
    public void showEmpty(boolean forceShow) {

    }

    @Override
    public void showProgressBar(boolean forceShow) {

    }

    @Override
    public void showMusic(List<MusicFileItem> musics) {

    }
}
