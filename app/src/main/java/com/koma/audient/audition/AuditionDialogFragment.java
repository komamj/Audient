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
package com.koma.audient.audition;

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

import com.koma.audient.AudientApplication;
import com.koma.audient.R;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.model.entities.Audient;
import com.koma.common.util.Constants;
import com.koma.common.util.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuditionDialogFragment extends DialogFragment implements AuditionContract.View, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = AuditionDialogFragment.class.getSimpleName();

    @BindView(R.id.tv_actor_name)
    TextView mActorName;
    @BindView(R.id.tv_music_name)
    TextView mMusicName;
    @BindView(R.id.progress_bar)
    AppCompatSeekBar mProgressBar;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.iv_pause)
    ImageView mPauseButton;

    private String mId;

    @OnClick(R.id.iv_pause)
    void doPauseOrPlay() {
        if (mPresenter != null) {
            mPresenter.doPauseOrPlay();
        }
    }

    @Inject
    AuditionPresenter mPresenter;

    public AuditionDialogFragment() {
    }

    public static AuditionDialogFragment newInstance(String id) {
        AuditionDialogFragment fragment = new AuditionDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_AUDIENT_ID, id);

        fragment.setArguments(bundle);

        return fragment;
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
            mId = getArguments().getString(Constants.KEY_AUDIENT_ID);
        }

        // inject presenter
        DaggerAuditionComponent.builder()
                .audientRepositoryComponent(((AudientApplication) getActivity().getApplication()).getRepositoryComponent())
                .auditionPresenterModule(new AuditionPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_audition, container, false);

        ButterKnife.bind(this, view);

        mProgressBar.setOnSeekBarChangeListener(this);

        mPresenter.loadAudient(mId);

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
    public void onLoadFinished(Audient audient) {
        GlideApp.with(this).load(audient)
                .thumbnail(0.1f)
                .placeholder(new ColorDrawable(Color.GRAY))
                .dontAnimate()
                .into(mAlbum);
        mActorName.setText(audient.singer.get(0).name);
        mMusicName.setText(audient.name);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
