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
package com.xinshang.audient.feedback;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.base.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by koma on 3/28/18.
 */

public class FeedbackDialogFragment extends BaseDialogFragment implements FeedbackContract.View {
    private static final String DIALOG_TAG = "dialog_feedback";

    @BindView(R.id.tv_title)
    EditText mTitle;
    @BindView(R.id.tv_content)
    EditText mContent;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @BindString(R.string.error_title_message)
    String mEmptyTitleMessage;
    @BindString(R.string.error_content_message)
    String mEmptyContentMessage;

    @OnClick(R.id.fab)
    void sendFeedback() {
        if (mPresenter != null) {
            mPresenter.sendFeedback(mTitle.getText().toString(),
                    mContent.getText().toString());
        }
    }

    @Inject
    FeedbackPresenter mPresenter;

    public static void show(FragmentManager fragmentManager) {
        FeedbackDialogFragment dialogFragment = new FeedbackDialogFragment();
        dialogFragment.show(fragmentManager, DIALOG_TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AuditionDilogTheme);

        // inject presenter
        DaggerFeedbackComponent.builder()
                .audientRepositoryComponent(((AudientApplication) getActivity().getApplication()).getRepositoryComponent())
                .feedbackPresenterModule(new FeedbackPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_feedback, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLoadingIndicator(false);

        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void setPresenter(FeedbackContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadingIndicator(boolean isActive) {
        if (isActive) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }

    @Override
    public void showSuccessfulMessage() {
        Snackbar.make(mFab, R.string.feedback_successful_message, Snackbar.LENGTH_SHORT)
                .show();

        mFab.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 1000);
    }

    @Override
    public void showFailedMessage() {
        Snackbar.make(mFab, R.string.feedback_failed_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showErrorTitle() {
        mTitle.setError(mEmptyTitleMessage);
        mTitle.setFocusable(true);
        mTitle.setFocusableInTouchMode(true);
        mTitle.requestFocus();
    }

    @Override
    public void showErrorContent() {
        mContent.setError(mEmptyContentMessage);
        mContent.setFocusable(true);
        mContent.setFocusableInTouchMode(true);
        mContent.requestFocus();
    }
}
