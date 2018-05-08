package nl.wolfpackit.wolfpack.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TravelDeclarationDao {
    @Query("SELECT * FROM travelDeclaration")
    List<TravelDeclaration> getAll();

    @Query("SELECT * FROM travelDeclaration WHERE ID IN (:ids)")
    List<TravelDeclaration> get(String[] ids);

    @Query("SELECT * FROM travelDeclaration WHERE ID =:ID LIMIT 1")
    TravelDeclaration get(String ID);

    @Query("SELECT * FROM travelDeclaration WHERE declarationID =:ID LIMIT 1")
    TravelDeclaration findByDeclarationID(String ID);

    @Insert
    void insertAll(TravelDeclaration... travelDeclaration);

    @Delete
    void delete(TravelDeclaration travelDeclaration);

    @Update
    void update(TravelDeclaration travelDeclaration);
}
