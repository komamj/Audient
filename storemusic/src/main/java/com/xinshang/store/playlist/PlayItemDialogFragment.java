package com.xinshang.store.playlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.xinshang.store.base.BaseDialogFragment;
import com.xinshang.store.utils.LogUtils;

public class PlayItemDialogFragment extends BaseDialogFragment {
    private static final String TAG = "PlayItemDialogFragment";

    private static final String DIALOG_TITLE = "dialog_title";

    private String mTitle;

    private OnConfirmListener mListener;

    public static void showDialog(FragmentManager fm, OnConfirmListener listener, String title) {
        final PlayItemDialogFragment dialog = new PlayItemDialogFragment();
        dialog.setConfirmListener(listener);
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_TITLE, title);
        dialog.setArguments(bundle);

        dialog.show(fm, TAG);
    }

    public void setConfirmListener(OnConfirmListener listener) {
        mListener = listener;
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
