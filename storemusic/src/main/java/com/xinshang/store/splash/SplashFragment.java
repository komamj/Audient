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
package com.xinshang.store.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.xinshang.store.R;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.main.MainActivity;
import com.xinshang.store.utils.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by koma on 3/5/18.
 */

public class SplashFragment extends BaseFragment implements SplashContract.View {
    private static final String TAG = SplashFragment.class.getSimpleName();

    @BindView(R.id.tv_user_name)
    AutoCompleteTextView mUserName;
    @BindView(R.id.tv_password)
    EditText mPassword;
    @BindView(R.id.card_view)
    CardView mCardView;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;

    private SplashContract.Presenter mPresenter;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();

        if (mPresenter != null) {
            mPresenter.login(userName, password);
        }

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
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

        setLoadingIndicatorView(false);

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
    public void setLoadingIndicatorView(boolean isActive) {
        if (isActive) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }

    @Override
    public void showLoginView(boolean forceShow) {
        if (forceShow) {
            mCardView.setVisibility(View.VISIBLE);
        } else {
            mCardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSuccessfullyLoginedMessage() {
        if (getView() != null) {
            Snackbar.make(getView(), R.string.login_successful_message, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void showLoginError() {
        if (getView() != null) {
            Snackbar.make(getView(), R.string.login_error_message, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}
