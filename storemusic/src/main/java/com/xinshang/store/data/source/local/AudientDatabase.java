package com.xinshang.store.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.xinshang.store.data.entities.TencentMusic;

@Database(entities = {TencentMusic.class}, version = 1000)
public abstract class AudientDatabase extends RoomDatabase {
    public abstract AudientDao audientDao();
}
