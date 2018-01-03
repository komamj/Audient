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
package com.koma.audient.login;

import android.content.Intent;
import android.widget.EditText;

import com.koma.audient.R;
import com.koma.audient.main.MainActivity;
import com.koma.common.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by koma on 1/3/18.
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {
    @BindView(R.id.user_name)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;

    @OnClick(R.id.submit)
    void doLogin() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        ((LoginActivity) mContext).overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        ((LoginActivity) mContext).finish();
    }

    private LoginContract.Presenter mPresenter;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
