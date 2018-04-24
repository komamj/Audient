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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.base.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by koma on 3/8/18.
 */

public class LoginDialogFragment extends BaseDialogFragment implements LoginContract.View {
    private static final String DIALOG_TAG = "login_dialog";

    @BindView(R.id.cb_wechat)
    RadioButton mRadioButton;

    @Inject
    LoginPresenter mPresenter;

    @OnCheckedChanged(R.id.cb_wechat)
    void onCheckedChange() {

    }

    public static void show(FragmentManager fragmentManager) {
        LoginDialogFragment fragment = new LoginDialogFragment();
        fragment.show(fragmentManager, DIALOG_TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inject presenter layer
        DaggerLoginComponent.builder().audientRepositoryComponent(
                ((AudientApplication) (getActivity().getApplication())).getRepositoryComponent())
                .loginPresenterModule(new LoginPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_login,
                null, false);
        ButterKnife.bind(this, view);

        builder.setTitle(R.string.login_label);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPresenter != null) {
                    mPresenter.sendLoginRequest();
                }
            }
        });

        return builder.create();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadIndicator(boolean active) {

    }

    @Override
    public void showSuccessfulMessage() {

    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void onLoginWXCompleted() {

    }
}
