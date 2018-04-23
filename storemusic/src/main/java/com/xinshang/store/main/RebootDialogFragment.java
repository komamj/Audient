package com.xinshang.store.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinshang.store.R;
import com.xinshang.store.base.BaseDialogFragment;
import com.xinshang.store.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by koma on 4/23/18.
 */

public class RebootDialogFragment extends BaseDialogFragment {
    private static final String TAG = "RebootDialogFragment";

    @OnClick(R.id.tv_comfirm)
    void onConfirm() {
        Intent intent = new Intent(Constants.ACTION_REBOOT);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        this.dismiss();
    }

    @OnClick(R.id.tv_cancel)
    void onCancel() {
        this.dismiss();
    }

    public static void showRebootDialog(FragmentManager fragmentManager) {
        RebootDialogFragment dialogFragment = new RebootDialogFragment();

        dialogFragment.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.AuditionDilogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reboot, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
}
