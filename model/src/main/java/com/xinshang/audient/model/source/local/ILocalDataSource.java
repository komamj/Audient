package com.xinshang.audient.model.source.local;

/**
 * Created by koma on 3/6/18.
 */

public interface ILocalDataSource {
    void persistenceLoginInfo(String code, String token, String refreshToken);
}
