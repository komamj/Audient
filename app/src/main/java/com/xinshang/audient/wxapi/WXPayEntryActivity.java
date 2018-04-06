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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.util.WXEntryMessageEvent;
import com.xinshang.audient.util.WXPayEntryMessageEvent;
import com.xinshang.common.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by koma on 3/5/18.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXPayEntryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        ((AudientApplication) getApplication()).getWXAPI()
                .handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);

        LogUtils.i(TAG, "onNewIntent");

        setIntent(newIntent);

        ((AudientApplication) getApplication()).getWXAPI()
                .handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp response) {
        if (response.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            LogUtils.i(TAG, "onResp " + response.errCode);
            EventBus.getDefault().post(new WXPayEntryMessageEvent(response));
            finish();
        }
    }
}
