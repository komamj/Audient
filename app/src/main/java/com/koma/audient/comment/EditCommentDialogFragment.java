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
package com.koma.audient.comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koma.audient.R;
import com.koma.audient.model.entities.Comment;
import com.koma.audient.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCommentDialogFragment extends BottomSheetDialogFragment {
    private static final String TAG = EditCommentDialogFragment.class.getSimpleName();

    @BindView(R.id.edit_text)
    AppCompatEditText mEditText;

    @OnClick(R.id.iv_send)
    void onSendClick() {
        Comment comment = new Comment();
        comment.message = mEditText.getText().toString();
        comment.time = Utils.getTimeStamp();
        comment.userName = "Koma";

        mEditText.setText("");
        mEditText.clearFocus();

        dismiss();
    }

    public static EditCommentDialogFragment newInstance() {
        EditCommentDialogFragment fragment = new EditCommentDialogFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_comment, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
}
