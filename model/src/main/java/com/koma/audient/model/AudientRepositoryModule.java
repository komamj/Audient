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
package com.koma.audient.model;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.koma.audient.model.source.AudientDataSource;
import com.koma.audient.model.source.local.AudientDao;
import com.koma.audient.model.source.local.AudientDatabase;
import com.koma.audient.model.source.local.LocalDataSource;
import com.koma.audient.model.source.remote.RemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by koma on 1/3/18.
 */
@Module
public class AudientRepositoryModule {
    private static final String DB_NAME = "audient-db";

    @Singleton
    @Provides
    AudientDataSource provideLocalDataSource(Context context, AudientDao audientDao,
                                             SharedPreferences sharedPreferences) {
        return new LocalDataSource(context, audientDao, sharedPreferences);
    }

    @Singleton
    @Provides
    AudientDataSource provideRemoteDataSource() {
        return new RemoteDataSource();
    }

    @Singleton
    @Provides
    SharedPreferences provideSharePreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences;
    }

    @Singleton
    @Provides
    AudientDatabase provideDb(Context context) {
        return Room.databaseBuilder(context, AudientDatabase.class, DB_NAME).build();
    }

    @Singleton
    @Provides
    AudientDao provideAudientDao(AudientDatabase db) {
        return db.audientDao();
    }
}
