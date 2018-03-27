package com.example.mzfirstspam;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by s147356 on 27-3-2018.
 */
@Database(entities = {fence.class}, version = 1)
public abstract class FenceDB extends RoomDatabase{
    public abstract FenceDao fenceDao();
}
