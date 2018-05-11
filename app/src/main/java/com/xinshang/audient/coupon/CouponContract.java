package com.xinshang.audient.coupon;

import com.xinshang.common.base.BasePresenter;
import com.xinshang.common.base.BaseView;

/**
 * Created by koma on 5/11/18.
 */

public interface CouponContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void setLoadingIndicator(boolean isActive);

        void showSuccessfulMessage();

        void showFailedMessage();
    }

    interface Presenter extends BasePresenter {
        void loadMyCoupons(String freeCode);
    }
}
