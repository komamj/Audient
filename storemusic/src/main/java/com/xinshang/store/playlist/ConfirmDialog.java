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
package com.xinshang.store.playlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.xinshang.store.utils.LogUtils;

/**
 * Created by koma on 3/22/18.
 */

public class ConfirmDialog extends DialogFragment {
    private static final String TAG = ConfirmDialog.class.getSimpleName();

    private static final String TAG_CONFIRM = "tag_confirm";

    private static final String DIALOG_TITLE = "dialog_title";

    private Context mContext;

    private String mTitle;

    private OnConfirmListener mListener;

    public static void showConfirmDialog(FragmentManager fm, OnConfirmListener listener,
                                         String title) {
        final ConfirmDialog dialog = new ConfirmDialog();
        dialog.setConfirmListener(listener);
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_TITLE, title);
        dialog.setArguments(bundle);

        dialog.show(fm, TAG_CONFIRM);
    }

    public void setConfirmListener(OnConfirmListener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mTitle = getArguments().getString(DIALOG_TITLE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(mTitle);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onConfirm();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}
