package com.xinshang.store.data.source.local;

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
}
