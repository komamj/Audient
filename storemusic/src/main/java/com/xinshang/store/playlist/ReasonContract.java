package com.xinshang.store.playlist;

import com.xinshang.store.base.BasePresenter;
import com.xinshang.store.base.BaseView;

public interface ReasonContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void setLoadingIndicator(boolean isActive);

        void dismissReasonDialogFragment();
    }

    interface Presenter extends BasePresenter {
        void deleteStoreSong(String id, String reason);
    }
}
