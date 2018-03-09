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
package com.xinshang.audient.wxapi;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;

import com.xinshang.audient.R;
import com.xinshang.common.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by koma on 3/9/18.
 */

public class WXEntryFragment extends BaseFragment implements WXEntryContract.View {
    private static final String TAG = WXEntryFragment.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;

    private WXEntryContract.Presenter mPresenter;

    public static WXEntryFragment newInstance() {
        return new WXEntryFragment();
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
        return R.layout.fragment_wxentry;
    }

    @Override
    public void setPresenter(WXEntryContract.Presenter presenter) {
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

        WXEntryActivity wxEntryActivity = (WXEntryActivity) mContext;
        wxEntryActivity.finish();
        wxEntryActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void showLoadingError() {
        if (getView() != null) {
            Snackbar.make(getView(), R.string.login_error_message, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}
