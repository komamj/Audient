package com.xinshang.audient.model.source.local;

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
}
