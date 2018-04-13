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
package com.xinshang.store.audition;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuditionDialogFragment extends DialogFragment implements AuditionContract.View {
    private static final String TAG = AuditionDialogFragment.class.getSimpleName();

    @BindView(R.id.tv_artist_name)
    TextView mActorName;
    @BindView(R.id.tv_name)
    TextView mMusicName;
    @BindView(R.id.progress_bar)
    AppCompatSeekBar mProgressBar;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.iv_pause)
    ImageView mPauseButton;
    @Inject
    AuditionPresenter mPresenter;
    private Song mAudient;

    public AuditionDialogFragment() {
    }

    public static AuditionDialogFragment newInstance(Song audient) {
        AuditionDialogFragment fragment = new AuditionDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_AUDIENT, audient);

        fragment.setArguments(bundle);

        return fragment;
    }

    @OnClick(R.id.iv_pause)
    void doPauseOrPlay() {
        if (mPresenter != null) {
            mPresenter.doPauseOrPlay();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AuditionDilogTheme);

        if (getArguments() != null) {
            mAudient = getArguments().getParcelable(Constants.KEY_AUDIENT);
        }

        // inject presenter
        DaggerAuditionComponent.builder()
                .audientRepositoryComponent(((StoreMusicApplication) getActivity().getApplication()).getRepositoryComponent())
                .auditionPresenterModule(new AuditionPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_audition, container, false);

        ButterKnife.bind(this, view);

        mProgressBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (mPresenter != null) {
                            mPresenter.seekTo(userSelectedPosition);
                        }
                    }
                });

        if (mPresenter != null) {
            mPresenter.subscribe();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LogUtils.i(TAG, "onDestroy");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void setPresenter(AuditionContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public String getAudientId() {
        return this.mAudient.mediaId;
    }

    @Override
    public void showAudient(Song audient) {
        GlideApp.with(this).load(audient)
                .thumbnail(0.1f)
                .placeholder(new ColorDrawable(Color.GRAY))
                .dontAnimate()
                .into(mAlbum);
        mActorName.setText(audient.artistName);
        mMusicName.setText(audient.mediaName);
    }

    @Override
    public void setMaxProgress(int progress) {
        LogUtils.i(TAG, "setMaxProgress :" + TimeUnit.SECONDS.toMillis(progress));

        mProgressBar.setMax(progress);
    }

    @Override
    public void showProgress(int progress) {
        LogUtils.i(TAG, "showProgress :" + progress);

        mProgressBar.setProgress(progress);
    }

    @Override
    public void showSecondaryProgress(int progress) {
        LogUtils.i(TAG, "showSecondaryProgress :" + progress);

        mProgressBar.setSecondaryProgress(progress);
    }

    @Override
    public void updateControllView(int playState) {
        if (playState == Constants.PLAYING) {
            mPauseButton.setImageResource(R.drawable.ic_pause);
        } else if (playState == Constants.PAUSED) {
            mPauseButton.setImageResource(R.drawable.ic_play);
        } else if (playState == Constants.COMPLETED) {
            mPauseButton.setImageResource(R.drawable.ic_replay);
        }
    }

    @Override
    public void dismissAuditionDialog() {
        dismiss();
    }
}
