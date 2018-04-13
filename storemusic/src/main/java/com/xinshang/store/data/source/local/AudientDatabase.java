package com.xinshang.store.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.xinshang.store.data.entities.Song;

@Database(entities = {Song.class}, version = 1000)
public abstract class AudientDatabase extends RoomDatabase {
    public abstract SongDao songDao();
}
