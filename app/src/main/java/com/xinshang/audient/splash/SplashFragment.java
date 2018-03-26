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
package com.xinshang.audient.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;

import com.xinshang.audient.R;
import com.xinshang.audient.login.LoginDialogFragment;
import com.xinshang.audient.main.MainActivity;
import com.xinshang.audient.store.StoresDialogFragment;
import com.xinshang.common.base.BaseFragment;
import com.xinshang.common.util.LogUtils;

import butterknife.BindView;

/**
 * Created by koma on 3/5/18.
 */

public class SplashFragment extends BaseFragment implements SplashContract.View {
    private static final String TAG = SplashFragment.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;

    private SplashContract.Presenter mPresenter;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void showMainView() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        SplashActivity activity = (SplashActivity) mContext;
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        activity.finish();
    }

    @Override
    public void showLoginDialog() {
        LoginDialogFragment.show(getChildFragmentManager());
    }

    @Override
    public void showStoresDialog() {
        StoresDialogFragment.show(getChildFragmentManager());
    }

    @Override
    public void showSuccessfulMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.loading_successful_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showLoadingError() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.loading_error_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void setLoadingIndicator(boolean isActive) {
        if (isActive) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }
}
