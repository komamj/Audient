package com.xinshang.audient.hint;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.base.BaseDialogFragment;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.payment.PaymentDialogFragment;
import com.xinshang.common.util.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HintDialogFragment extends BaseDialogFragment {
    private static final String TAG = "HintDialogFragment";

    @OnClick(R.id.tv_comfirm)
    void onConfirmClicked() {
        PaymentDialogFragment.show(getFragmentManager(), mAudient);

        this.dismiss();
    }

    @OnClick(R.id.tv_ignore)
    void onIgnoreClicked() {
        ((AudientApplication) mContext.getApplicationContext())
                .getRepositoryComponent()
                .getRepository()
                .persistenceDemandStatus(false);

        PaymentDialogFragment.show(getFragmentManager(), mAudient);

        this.dismiss();
    }

    private Audient mAudient;

    public static void show(FragmentManager fragmentManager, Audient audient) {
        HintDialogFragment dialogFragment = new HintDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_AUDIENT, audient);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AuditionDilogTheme);

        if (getArguments() != null) {
            mAudient = getArguments().getParcelable(Constants.KEY_AUDIENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_hint, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
