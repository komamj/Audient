package com.xinshang.audient.coupon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.base.BaseDialogFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by koma on 5/11/18.
 */

public class CouponDialogFragment extends BaseDialogFragment implements CouponContract.View {
    private static final String DIALOG_TAG = "dialog_coupon";

    @Inject
    CouponPresenter mPresenter;

    @OnClick(R.id.tv_comfirm)
    void onConfirmClicked() {
        this.dismiss();
    }

    @OnClick(R.id.tv_cancell)
    void onCancellClicked() {
        this.dismiss();
    }

    public static void show(FragmentManager fragmentManager) {
        CouponDialogFragment dialogFragment = new CouponDialogFragment();
        dialogFragment.show(fragmentManager, DIALOG_TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AuditionDilogTheme);

        // inject presenter
        DaggerCouponComponent.builder()
                .audientRepositoryComponent(((AudientApplication) getActivity().getApplication())
                        .getRepositoryComponent())
                .couponPresenterModule(new CouponPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_coupon, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadingIndicator(boolean isActive) {

    }

    @Override
    public void showSuccessfulMessage() {

    }

    @Override
    public void showFailedMessage() {

    }

    @Override
    public void setPresenter(CouponContract.Presenter presenter) {

    }
}
