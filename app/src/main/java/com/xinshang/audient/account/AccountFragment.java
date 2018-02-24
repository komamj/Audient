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
package com.xinshang.audient.account;

import android.os.Bundle;
import android.view.View;

import com.xinshang.common.base.BaseFragment;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

public class AccountFragment extends BaseFragment implements AccountContract.View {
    private static final String TAG = AccountFragment.class.getSimpleName();

    private AccountContract.Presenter mPresenter;

    private long mId;

    public AccountFragment() {
    }

    public static AccountFragment newInstance(long id) {
        AccountFragment fragment = new AccountFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.KEY_AUDIENT, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mId = getArguments().getLong(Constants.KEY_AUDIENT);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void setPresenter(AccountContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
