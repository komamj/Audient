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
package com.xinshang.audient.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.common.base.BaseActivity;
import com.xinshang.common.util.ActivityUtils;
import com.xinshang.common.util.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by koma on 3/2/18.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final String TAG = WXEntryActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    WXEntryPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");
    }

    @Override
    protected void onPermissonGranted() {
        setSupportActionBar(mToolbar);

        ((AudientApplication) getApplication()).getWXAPI()
                .handleIntent(getIntent(), this);

        WXEntryFragment fragment = (WXEntryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);
        if (fragment == null) {
            fragment = WXEntryFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.content_main);
        }

        // inject presenter layer
        DaggerWXEntryComponent.builder().audientRepositoryComponent(
                ((AudientApplication) getApplication()).getRepositoryComponent())
                .wXEntryPresenterModule(new WXEntryPresenterModule(fragment))
                .build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);

        LogUtils.i(TAG, "onNewIntent");

        setIntent(newIntent);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.i(TAG, "onResp " + baseResp.errCode);

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
                String code = newResp.code;
                mPresenter.loadAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
    }
}
