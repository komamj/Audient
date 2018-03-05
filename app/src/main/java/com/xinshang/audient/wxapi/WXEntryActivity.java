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

import com.xinshang.common.util.LogUtils;

/**
 * Created by koma on 3/2/18.
 */

public class WXEntryActivity extends Activity /*implements IWXAPIEventHandler*/ {
    private static final String TAG = WXEntryActivity.class.getSimpleName();

   // private IWXAPI mWeChatApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(TAG, "onCreate");
       /* mWeChatApi = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID, true);
        mWeChatApi.registerApp(Constants.WECHAT_APP_ID);
        mWeChatApi.handleIntent(getIntent(), this);*/
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        setIntent(newIntent);
    }

   /* @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.i(TAG, "onResp " + baseResp.errCode);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp newResp = (SendAuth.Resp) baseResp;
                EventBus.getDefault().post(new WeChatMessageEvent(newResp.code));
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
        finish();
    }*/
}
