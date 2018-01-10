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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koma.audient.AudientApplication;
import com.koma.audient.R;
import com.koma.audient.helper.GlideApp;
import com.koma.audient.model.entities.MusicFileItem;
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

    private MusicFileItem mMusicFileItem;

    @OnClick(R.id.iv_pause)
    void doPauseOrPlay() {

    }

    @Inject
    AuditionPresenter mPresenter;

    public AuditionDialogFragment() {
    }

    public static AuditionDialogFragment newInstance(MusicFileItem musicFileItem) {
        AuditionDialogFragment fragment = new AuditionDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_MUSIC_FILE_ITEM, musicFileItem);

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

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AuditionDilogTheme);

        if (getArguments() != null) {
            mMusicFileItem = getArguments().getParcelable(Constants.KEY_MUSIC_FILE_ITEM);
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

        mPresenter.loadAlbumUrl(mMusicFileItem);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mActorName.setText(mMusicFileItem.actorName);
        mMusicName.setText(mMusicFileItem.musicName);
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
    public void onLoadAlbumUrlFinished(String url) {
        GlideApp.with(this).load(url).into(mAlbum);
    }
}
