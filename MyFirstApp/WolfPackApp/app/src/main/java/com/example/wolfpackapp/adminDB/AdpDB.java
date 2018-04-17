package com.example.wolfpackapp.adminDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.wolfpackapp.Database.*;
import com.example.wolfpackapp.Database.EmpDAO;
import com.example.wolfpackapp.Database.Employee;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Database(entities = {Admin.class}, version = 1,  exportSchema = false)
public abstract class AdpDB extends RoomDatabase {
    public abstract AdDAO AdDAO();
}
