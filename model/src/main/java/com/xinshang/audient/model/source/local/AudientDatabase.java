package com.xinshang.audient.model.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.xinshang.audient.model.entities.Audient;

@Database(entities = {Audient.class}, version = 1000)
public abstract class AudientDatabase extends RoomDatabase {
    public abstract AudientDao audientDao();
}
