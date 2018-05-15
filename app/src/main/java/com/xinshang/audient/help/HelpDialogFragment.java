package com.xinshang.audient.help;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinshang.audient.R;
import com.xinshang.audient.base.BaseDialogFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by koma on 5/15/18.
 */

public class HelpDialogFragment extends BaseDialogFragment {
    private static final String TAG = "HelpDialogFragment";

    @OnClick(R.id.tv_comfirm)
    void doConfirm() {
        this.dismiss();
    }

    public static void show(FragmentManager fragmentManager) {
        HelpDialogFragment dialogFragment = new HelpDialogFragment();
        dialogFragment.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AuditionDilogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_help, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
}
