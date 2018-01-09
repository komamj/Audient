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
package com.koma.audient.dialog.audition;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koma.audient.R;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.model.entities.Audient;
import com.koma.common.util.Constants;
import com.koma.common.util.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuditionDialogFragment extends DialogFragment implements AuditionContract.View {
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

    private Audient mAudient;

    @OnClick(R.id.iv_pause)
    void doPauseOrPlay() {

    }

    @Inject
    AuditionPresenter mPresenter;

    public AuditionDialogFragment() {
    }

    public static AuditionDialogFragment newInstance(Audient audient) {
        AuditionDialogFragment fragment = new AuditionDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_AUDITION, audient);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AuditionDilogTheme);

        if (getArguments() != null) {
            mAudient = getArguments().getParcelable(Constants.KEY_AUDITION);
        }

        // inject presenter
        DaggerAuditionComponent.builder()
                .auditionPresenterModule(new AuditionPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_audition, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        GlideApp.with(this).load(mAudient.albumUrl).into(mAlbum);
        mActorName.setText(mAudient.actorName);
        mMusicName.setText(mAudient.musicName);
    }

    @Override
    public void onResume() {
        super.onResume();

        LogUtils.i(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtils.i(TAG, "onDestroy");
    }

    @Override
    public void setPresenter(AuditionContract.Presenter presenter) {

    }
}
