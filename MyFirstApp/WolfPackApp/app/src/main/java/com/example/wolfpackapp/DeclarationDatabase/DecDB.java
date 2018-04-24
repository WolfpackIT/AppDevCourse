package com.example.wolfpackapp.DeclarationDatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.example.wolfpackapp.Database.*;
import com.example.wolfpackapp.Database.Employee;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Database(entities = {com.example.wolfpackapp.DeclarationDatabase.Declaration.class, com.example.wolfpackapp.DeclarationDatabase.DeclarationOther.class, com.example.wolfpackapp.DeclarationDatabase.DeclarationCar.class}, version = 4,  exportSchema = false)
public abstract class DecDB extends RoomDatabase {
    public abstract DecDAO DecDAO();

}
