package com.example.wolfpackapp.DeclarationDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.wolfpackapp.Database.*;
import com.example.wolfpackapp.Database.Employee;

import java.util.List;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Dao
public interface DecDAO {
    @Query("SELECT * FROM Declaration")
    List<Declaration> getAll();

    @Query("SELECT * FROM Declaration WHERE uid IN (:userIds)")
    List<Declaration> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Declaration WHERE email IN (:email)")
    List<Declaration> getList(String email);

    @Query("SELECT * FROM Declaration WHERE email IN (:email) AND checked == (:checked)")
    List<Declaration> getCheckedList(String email, Boolean checked);

    @Query("SELECT * FROM Declaration WHERE email IN (:email) AND title == (:title) AND timestamp = (:td) AND money == (:mon)")
    List<Declaration> getDetailedList(String email, String title, String td, double mon );

    @Query("SELECT * FROM Declaration WHERE checked == (:checked)")
    List<Declaration> getFullList(Boolean checked);

    @Insert
    void insertAll(Declaration... decs);

    @Insert
    void insert(Declaration dec);

    @Delete
    void delete(Declaration dec);

    @Delete
    void deletes(Declaration... decs);
}
