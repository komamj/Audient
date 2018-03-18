package com.xinshang.store.data.source.local;

import com.xinshang.store.data.entities.CommandResponse;

import io.reactivex.Flowable;

/**
 * Created by koma on 3/6/18.
 */

public interface ILocalDataSource {
    void persistenceLoginStatus(boolean loginStatus);

    boolean getLoginStatus();

    void persistenceAccessToken(String accessToken);

    String getAccessToken();

    void persistenceRefreshToken(String refreshToken);

    String getRefreshToken();

    void persistenceStoreId(String storeId);

    String getStoreId();

    Flowable<CommandResponse> parsingCommandResponse(String response);
}
