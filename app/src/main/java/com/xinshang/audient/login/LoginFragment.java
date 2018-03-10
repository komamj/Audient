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
package com.xinshang.audient.login;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xinshang.audient.R;
import com.xinshang.common.base.BaseFragment;
import com.xinshang.common.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by koma_20 on 2018/3/3.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {
    private static final String TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;

    private LoginContract.Presenter mPresenter;

    @OnClick(R.id.btn_wx)
    void loginWeChat() {
        setLoadIndicator(true);

        if (mPresenter != null) {
            mPresenter.sendLoginRequest();
        }
    }


    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(TAG, "onCreate");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLoadIndicator(false);

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
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadIndicator(boolean active) {
        if (active) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }

    @Override
    public void showSuccessfulMessage() {
        if (getView() != null) {
            Snackbar.make(getView(), R.string.login_successful_message, Snackbar.LENGTH_SHORT)
                    .show();
        }

        onLoginWXCompleted();
    }

    @Override
    public void showLoadingError() {
        if (getView() != null) {
            Snackbar.make(getView(), R.string.login_error_message, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onLoginWXCompleted() {
        AppCompatActivity activity = (AppCompatActivity) mContext;
        activity.finish();
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}