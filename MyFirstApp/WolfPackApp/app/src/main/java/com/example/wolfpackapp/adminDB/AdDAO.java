package com.example.wolfpackapp.adminDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.wolfpackapp.Database.Employee;

import java.util.List;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Dao
public interface AdDAO {
    @Query("SELECT * FROM Admin")
    List<Admin> getAll();

    @Query("SELECT * FROM Admin WHERE uid IN (:userIds)")
    List<Admin> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Admin WHERE uid == (:userIds)")
    List<Admin> uidList(String userIds);

    @Query("SELECT * FROM Admin WHERE email LIKE :first LIMIT 1")
    Admin findByName(String first);

    @Insert
    void insertAll(Admin... admin);

    @Insert
    void insert(Admin ad);

    @Delete
    void delete(Admin admin);
}