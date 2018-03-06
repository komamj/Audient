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
package com.xinshang.store.comment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCommentDialogFragment extends BottomSheetDialogFragment
        implements EditCommentContract.View {
    private static final String TAG = EditCommentDialogFragment.class.getSimpleName();
    private static final String DIALOG_TAG = "edit_comment_dialog";

    @BindView(R.id.edit_text)
    AppCompatEditText mEditText;
    @Inject
    EditCommentPresenter mPresenter;
    private Context mContext;
    private TencentMusic mAudient;

    public static void show(FragmentManager fragmentManager, TencentMusic audient) {
        final EditCommentDialogFragment fragment = new EditCommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_AUDIENT, audient);
        fragment.setArguments(bundle);
        fragment.show(fragmentManager, DIALOG_TAG);
    }

    @OnClick(R.id.iv_send)
    void onSendClick() {
        if (mPresenter != null) {
            mPresenter.addComment(mAudient, mEditText.getText().toString());
        }

        mEditText.setText("");
        mEditText.clearFocus();

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getDialog().getWindow().getDecorView().getWindowToken(), 0);

        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.i(TAG, "onAttach");
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAudient = getArguments().getParcelable(Constants.KEY_AUDIENT);
        }
        DaggerEditCommentComponent.builder()
                .audientRepositoryComponent(((StoreMusicApplication) getActivity().getApplication()).getRepositoryComponent())
                .editCommentPresenterModule(new EditCommentPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_comment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setPresenter(EditCommentContract.Presenter presenter) {

    }
}