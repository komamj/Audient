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
package com.xinshang.store.store;

import android.os.Bundle;
import android.view.View;

import com.xinshang.store.R;
import com.xinshang.store.base.BaseFragment;

/**
 * Created by koma_20 on 2018/3/1.
 */

public class StoreInfoFragment extends BaseFragment implements StoreInfoContract.View {
    private static final String TAG = StoreInfoFragment.class.getSimpleName();

    private StoreInfoContract.Presenter mPresenter;

    public StoreInfoFragment() {
    }

    public static StoreInfoFragment newInstance() {
        return new StoreInfoFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    public void setPresenter(StoreInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
