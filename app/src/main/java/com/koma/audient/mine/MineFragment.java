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
package com.koma.audient.mine;

import android.os.Bundle;
import android.view.View;

import com.koma.audient.AudientApplication;
import com.koma.audient.R;
import com.koma.audient.model.entities.MusicFileItem;
import com.koma.common.base.BaseFragment;
import com.koma.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

public class MineFragment extends BaseFragment implements MineContract.View {
    private static final String TAG = MineFragment.class.getSimpleName();

    private boolean mIsPrepared;

    @Inject
    MinePresenter mPresenter;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mIsPrepared) {
            if (mPresenter != null) {
                mPresenter.subscribe();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        DaggerMineComponent.builder()
                .audientRepositoryComponent(
                        (((AudientApplication) getActivity().getApplication()).getRepositoryComponent()))
                .minePresenterModule(new MinePresenterModule(this))
                .build()
                .inject(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mIsPrepared = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtils.i(TAG, "onDestroy");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {

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
