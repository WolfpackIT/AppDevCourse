package com.example.wolfpackapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Dao
public interface EmpDAO {
    @Query("SELECT * FROM Employee")
    List<Employee> getAll();

    @Query("SELECT * FROM Employee WHERE uid IN (:userIds)")
    List<Employee> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Employee WHERE uid == (:userIds)")
    List<Employee> uidList(String userIds);

    @Query("SELECT * FROM Employee WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    Employee findByName(String first, String last);

    @Insert
    void insertAll(Employee... employees);

    @Insert
    void insert(Employee emp);

    @Delete
    void delete(Employee employee);
}