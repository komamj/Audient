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
package com.xinshang.audient.payment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.helper.GlideApp;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.Coupon;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by koma on 3/1/18.
 */

public class PaymentDialogFragment extends BottomSheetDialogFragment implements
        PaymentContract.View {
    private static final String TAG = PaymentDialogFragment.class.getSimpleName();
    private static final String DIALOG_TAG = "dialog_payment";

    @BindView(R.id.tv_name)
    TextView mName;
    @BindView(R.id.tv_count)
    TextView mCount;
    @BindView(R.id.group_coupon)
    Group mGroupCoupon;
    @BindView(R.id.btn_confirm)
    Button mPay;
    @BindView(R.id.tv_free)
    TextView mFree;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;

    @OnClick(R.id.btn_confirm)
    void onConfirmClick() {
        if (mPresenter != null) {
            mPresenter.postOrder(mAudient);
        }
    }

    private Audient mAudient;

    public static void show(FragmentManager fragmentManager, Audient audient) {
        PaymentDialogFragment dialogFragment = new PaymentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_AUDIENT, audient);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentManager, DIALOG_TAG);
    }

    private Context mContext;

    @Inject
    PaymentPresenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAudient = getArguments().getParcelable(Constants.KEY_AUDIENT);
        }

        DaggerPaymentComponent.builder()
                .audientRepositoryComponent(((AudientApplication) getActivity().getApplication()).getRepositoryComponent())
                .paymentPresenterModule(new PaymentPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_payment, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setLoadingIndicator(false);

        GlideApp.with(this)
                .load(mAudient)
                .placeholder(new ColorDrawable(Color.GRAY))
                .thumbnail(0.1f)
                .circleCrop()
                .into(mAlbum);

        mName.setText(mAudient.mediaName);

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void setPresenter(PaymentContract.Presenter presenter) {

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
    public void setFreeIndicator(boolean free) {
        if (free) {
            mGroupCoupon.setVisibility(View.VISIBLE);
            mFree.setText(R.string.free_description);
            mPay.setText(R.string.confirm_free);
        } else {
            mGroupCoupon.setVisibility(View.GONE);
            mPay.setText(R.string.confirm_pay);
        }
    }

    @Override
    public void showSuccessfullyMessage() {
        if (getView() != null) {
            LogUtils.i(TAG,"showSuccessfullyMessage");
            Snackbar.make(mAlbum, "点播成功，请返回播放列表查看。", Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, @DismissEvent int event) {
                            PaymentDialogFragment.this.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void showFailedMessage() {
        if (getView() != null) {
            Snackbar.make(mAlbum, "点播失败，请稍后再试", Snackbar.LENGTH_SHORT)
                    .addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, @DismissEvent int event) {
                            PaymentDialogFragment.this.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    public void showCoupons(List<Coupon> coupons) {
        mCount.setText(mContext.getString(R.string.coupon_count, coupons.size()));
    }
}
