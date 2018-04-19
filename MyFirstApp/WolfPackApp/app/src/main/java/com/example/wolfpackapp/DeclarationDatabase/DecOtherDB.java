package com.example.wolfpackapp.DeclarationDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.wolfpackapp.Database.*;
import com.example.wolfpackapp.Database.Employee;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Database(entities = {com.example.wolfpackapp.DeclarationDatabase.DeclarationOther.class}, version = 1,  exportSchema = false)
public abstract class DecOtherDB extends RoomDatabase {
    public abstract DecOtherDAO DecOtherDAO();
}
