package com.example.wolfpackapp.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Database(entities = {Employee.class}, version = 1,  exportSchema = false)
public abstract class EmpDB extends RoomDatabase {
    public abstract EmpDAO EmpDAO();
}
