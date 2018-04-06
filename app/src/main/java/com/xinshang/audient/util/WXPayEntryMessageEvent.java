package com.xinshang.audient.util;

import com.tencent.mm.opensdk.modelbase.BaseResp;

public class WXPayEntryMessageEvent {
    private BaseResp mResp;

    public WXPayEntryMessageEvent(BaseResp resp) {
        this.mResp = resp;
    }

    public void setResp(BaseResp resp) {
        this.mResp = resp;
    }

    public BaseResp getResp() {
        return this.mResp;
    }
}
