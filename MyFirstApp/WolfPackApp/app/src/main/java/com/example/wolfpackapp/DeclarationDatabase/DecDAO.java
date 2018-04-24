package com.example.wolfpackapp.DeclarationDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.wolfpackapp.Database.*;
import com.example.wolfpackapp.Database.Employee;

import java.util.List;

/**
 * Created by Wolfpack on 4/3/2018.
 */

@Dao
public interface DecDAO {
    //Init query
//    @Query("SELECT * FROM declaration JOIN carDeclarations ON DeclarationIdentifier = CarDeclarationIdentifier")
//    List<Declaration> getCarDec();

    @Query("SELECT DISTINCT DeclarationIdentifier FROM declaration WHERE DeclarationIdentifier NOT IN ( SELECT d.DeclarationIdentifier FROM declaration as d  JOIN declaration as t ON d.DeclarationIdentifier < t.DeclarationIdentifier )")
    long getMaxDecID();

    @Query("SELECT MAX(DeclarationIdentifier) FROM declaration")
    long getMaxID();
    //initDeclaration only queries
    @Query("SELECT * FROM Declaration")
    List<Declaration> getAll();

    @Query("SELECT * FROM Declaration WHERE DeclarationIdentifier IN (:userIds)")
    List<Declaration> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM declaration WHERE DeclarationIdentifier = (:userIds)")
    Declaration loadSingleByIds(long userIds);

    @Query("SELECT * FROM Declaration WHERE email IN (:email)")
    List<Declaration> getList(String email);

    @Query("SELECT * FROM Declaration WHERE email IN (:email) AND checked = (:checked)")
    List<Declaration> getCheckedList(String email, Boolean checked);

    @Query("SELECT * FROM Declaration WHERE email IN (:email) AND title = (:title) AND timestamp = (:td) AND money = (:mon)")
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

    @Update
    void updateDecs(Declaration... decs);

    @Update
    void updateDec(Declaration dec);

    //Queries for the car table

    /**
     *
     * @return Cardeclarations and cardeclarations lists
     */
    @Query("SELECT * FROM carDeclarations")
    List<DeclarationCar> getAllCars();

    @Query("SELECT * FROM carDeclarations WHERE CarDeclarationIdentifier = (:userIds)")
    List<DeclarationCar> loadAllByCarIds(long userIds);


    @Query("SELECT MAX(carID) FROM carDeclarations")
    long getMaxcID();

    @Insert
    void insertAllcars(DeclarationCar... decs);

    @Insert
    void insertCar(DeclarationCar dec);

    @Delete
    void deleteCar(DeclarationCar dec);

    @Delete
    void deletesCars(DeclarationCar... decs);

    @Update
    void updateDecs(DeclarationCar... decsCar);

    @Update
    void updateDec(DeclarationCar decCar);

    //Queries for general table

    /**
     *
     * @return OtherDeclarations and OtherDeclarations list
     */

    @Query("SELECT * FROM otherdeclarations")
    List<DeclarationOther> getAllOthers();

    @Query("SELECT * FROM otherdeclarations WHERE OtherDeclarationIdentifier = (:userIds)")
    List<DeclarationOther> loadAllByOtherIds(long userIds);

    @Query("SELECT MAX(otherID) FROM otherdeclarations")
    long getMaxoID();

    @Insert
    void insertAllOthers(DeclarationOther... decs);

    @Insert
    void insertOther(DeclarationOther dec);

    @Delete
    void deleteOther(DeclarationOther dec);

    @Delete
    void deletesOthers(DeclarationOther... decs);

    @Update
    void updateDecs(DeclarationOther... decsOther);

    @Update
    void updateDec(DeclarationOther decOther);
}
